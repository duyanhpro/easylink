package easylink.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import easylink.common.Constant;
import easylink.dto.AlarmLevel;
import easylink.entity.Alarm;
import easylink.entity.DeviceSchema;
import easylink.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.PreDestroy;

@Service
public class StarrocksService {

    @Value("${raw.insert.batch.size}")
    int batchSize;// = 30;
    @Value("${raw.insert.batch.max-wait-time-second}")
    long batchMaxWaitTime;// = 200000; // 30 seconds
    private static final long QUEUE_POLL_TIME_MS = 1000; // 1 seconds

    private final Queue<Map<String, Object>> messageQueue = new ConcurrentLinkedQueue<>();
    Logger log = LoggerFactory.getLogger(this.getClass());
    HikariDataSource dataSource;

    // Provide the database connection URL, username, and password
    @Value("${raw.db.url}")
    String url;// = "jdbc:mysql://113.190.243.86:21930/easylink?useTimezone=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";      // starrocks
    @Value("${raw.db.username}")
    String username;// = "root";
    @Value("${raw.db.password}")
    String password;// = "";

    @Value("${raw.db.float.scale:2}")
    int floatScale;

    @Autowired
    DeviceSchemaService deviceSchemaService;

    public void insertData(Map<String, Object> data) {
        // TODO: find schema and redirect it to corresponding queue for each device type
        messageQueue.add(data);
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Create Starrocks connection pool");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);        // Set the maximum pool size (number of connections)
        config.setMinimumIdle(5);        // Set the minimum idle connections (optional)
        //try {
            dataSource = new HikariDataSource(config);
//        } catch (Exception e) {       // Application will stop if can not create hikari datasource (no connection)
//            log.error("Creating Hikari Datasource for Starrorck error", e);
//            return;
//        }

        log.info("Create thread to wait for data from queue and insert");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Create a batch for insertion
                LinkedList<Map<String, Object>> batch = new LinkedList<>();
                long first = System.currentTimeMillis();

                while (true) {
                    // Check if it is time to insert
                    if ((batch.size() >= batchSize) || ((System.currentTimeMillis() - first) >= (batchMaxWaitTime *1000))) {
                        if (!batch.isEmpty()) {
                            insertBatchIntoDatabase(batch);
                            // reset
                            batch.clear();
                        }
                    }

                    Map<String, Object> m = messageQueue.poll();
                    if (m == null) {
                        try {
                            Thread.sleep(QUEUE_POLL_TIME_MS);
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                        }
                        continue;
                    } else {
                        // add message to batch
                        if (batch.isEmpty())
                            first = System.currentTimeMillis();
                        batch.add(m);
                    }

                }
            }
        }).start();
    }

//    @Scheduled(fixedDelayString = "${raw.insert.batch.max-wait-time-second}", initialDelay = 5000)
//    public void scheduleInsertBatch() {
//
//    }

    @PreDestroy
    public void closeDataSource() {
        log.info("Clean up Starrocks connection pool");
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }

    public void insertAlarm(Alarm a) {
    // SQL query
        String insertQuery = "INSERT INTO alarm (device_token, event_time, content, type, level, rule_id, raw_event_time) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            // Set parameters
                preparedStatement.setString(1, a.getDeviceToken());
                //preparedStatement.setDate(2, a.getEventTime());
                preparedStatement.setTimestamp(2, new Timestamp(a.getEventTime().getTime()));   // never null
                preparedStatement.setString(3, a.getContent());
                preparedStatement.setString(4, a.getType());
                preparedStatement.setInt(5, a.getLevel().ordinal());
                preparedStatement.setObject(6, a.getRuleId());  // Can be null
                preparedStatement.setTimestamp(7, a.getRawEventTime() == null? null: new Timestamp(a.getRawEventTime().getTime()));

                // Execute the insert query
                int rowsAffected = preparedStatement.executeUpdate();
                log.trace("Inserted {} alarm record.", rowsAffected);

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Alarm>  searchAlarm(String deviceToken, Date startDate, Date endDate, Integer level, String type,
                                    int pageNumber, int pageSize) {
        // SQL query with parameters
        StringBuilder searchQuery =
                new StringBuilder("SELECT d.name as device_name,a.*  FROM alarm a, device d WHERE a.device_token = d.device_token ");
        if (deviceToken != null)
            searchQuery.append(" AND a.device_token = ?");
        if (startDate != null)
            searchQuery.append(" AND a.event_time >= ?");
        if (endDate != null)
            searchQuery.append(" AND a.event_time < ?");
        if (level != null)
            searchQuery.append(" AND a.level = ?");
        if (type != null)
            searchQuery.append(" AND a.type = ?");
        searchQuery.append(" AND a.device_token in (select device_token from user_device_view where user_id = ?) ");
        // Add paging parameters
        int offset = (pageNumber - 1) * pageSize;
        searchQuery.append(" order by a.event_time desc LIMIT ? OFFSET ? ");
        log.debug("Alarm search query: {}", searchQuery.toString());
        List<Alarm> resultList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(searchQuery.toString())) {

            int parameterIndex = 1;
            // Set parameters
            if (deviceToken != null)
                preparedStatement.setString(parameterIndex++, deviceToken);
            if (startDate!=null)
                preparedStatement.setTimestamp(parameterIndex++, new java.sql.Timestamp(startDate.getTime()));
            if (endDate != null)
                preparedStatement.setTimestamp(parameterIndex++, new java.sql.Timestamp(endDate.getTime()));
            if (level != null)
                preparedStatement.setInt(parameterIndex++, level);
            if (type!= null)
                preparedStatement.setString(parameterIndex++, type);

            preparedStatement.setInt(parameterIndex++, SecurityUtil.getUserDetail().getUserId());

            // Set paging parameters
            preparedStatement.setInt(parameterIndex++, pageSize);
            preparedStatement.setInt(parameterIndex, offset);

            // Execute the search query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve and process the results
                    //String retrievedDeviceToken = resultSet.getString("device_token");
                    String retrievedDeviceToken = resultSet.getString("device_name");   // use name instead
                    Timestamp retrievedEventTime = resultSet.getTimestamp("event_time");
                    String retrievedContent = resultSet.getString("content");
                    String retrievedType = resultSet.getString("type");
                    int retrievedLevel = resultSet.getInt("level");
                    Integer retrievedRuleId = resultSet.getInt("rule_id");
                    Timestamp retrievedRawEventTime = resultSet.getTimestamp("raw_event_time");

                    resultList.add(new Alarm(retrievedDeviceToken,
                            retrievedEventTime == null? null : new Date(retrievedEventTime.getTime()),
                            retrievedContent, retrievedType, AlarmLevel.values()[retrievedLevel], retrievedRuleId,
                            retrievedRawEventTime == null? null: new Date(retrievedRawEventTime.getTime())));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    public int  countAlarm(String deviceToken, Date startDate, Date endDate, Integer level, String type) {
        // SQL query with parameters
        StringBuilder searchQuery = new StringBuilder("SELECT count(*) FROM alarm WHERE 1=1 ");
        if (deviceToken != null)
            searchQuery.append(" AND device_token = ?");
        if (startDate != null)
            searchQuery.append(" AND event_time >= ?");
        if (endDate != null)
            searchQuery.append(" AND event_time < ?");
        if (level != null)
            searchQuery.append(" AND level = ?");
        if (type != null)
            searchQuery.append(" AND type = ?");
        searchQuery.append(" AND device_token in (select device_token from device where id in (select device_id from device_group where \n" +
                "group_id in (select group_id from user_group where user_id = ?))) ");
        List<Alarm> resultList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(searchQuery.toString())) {

            int parameterIndex = 1;
            // Set parameters
            if (deviceToken != null)
                preparedStatement.setString(parameterIndex++, deviceToken);
            if (startDate!=null)
                preparedStatement.setTimestamp(parameterIndex++, new java.sql.Timestamp(startDate.getTime()));
            if (endDate != null)
                preparedStatement.setTimestamp(parameterIndex++, new java.sql.Timestamp(endDate.getTime()));
            if (level != null)
                preparedStatement.setInt(parameterIndex++, level);
            if (type!= null)
                preparedStatement.setString(parameterIndex++, type);
            preparedStatement.setInt(parameterIndex++, SecurityUtil.getUserDetail().getUserId());

            // Execute the search query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
//    Map<String, String> schema = Map.of(
//            "device_token", "String",
//            "event_time", "Date",
//            "temp1", "Double",
//            "hum1", "Double",
//            "temp2", "Double",
//            "hum2", "Double",
//            "door1", "Integer",
//            "door2", "Integer",
//            "vol1", "Float",
//            "cur1", "Float",
//            "vol2", "Float",
//            "cur2", "Float",
//            "vol3", "Float",
//            "cur3", "Float",
//            "vol4", "Float",
//            "cur5", "Float"
//    );

    private void insertOne(Map<String, Object> data) {

        log.debug("InsertData");
        long start = System.currentTimeMillis();

        // Note: data already normalized
        // choose schema table from deviceToken
        String token = data.get(Constant.DEVICE_TOKEN).toString();
        DeviceSchema dt = deviceSchemaService.findFromDeviceToken(token);
        if (dt==null) {
            log.error("Device Type not found");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> schema;
        try {
            schema = mapper.readValue(dt.getDataSchema(), Map.class);
            log.debug("Find schema from device token {}, schema: {}", token, schema);
        } catch (Exception e) {
            log.error("Exception when getting schema: {}", e);    // should not happen! should validate schema before save
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            StringBuilder sqlBuilder = new StringBuilder("INSERT INTO " + dt.getTableName() + " (");
            StringBuilder valuesBuilder = new StringBuilder(") VALUES (");

            // Loop through the schema and build the SQL query dynamically
            for (Map.Entry<String, String> entry : schema.entrySet()) {
                sqlBuilder.append(entry.getKey()).append(",");
                valuesBuilder.append("?,");
            }

            // Remove the trailing commas and complete the SQL query
            sqlBuilder.setLength(sqlBuilder.length() - 1);
            valuesBuilder.setLength(valuesBuilder.length() - 1);
            sqlBuilder.append(valuesBuilder).append(")");

            //System.out.println("SQL: " + sqlBuilder);
            // Create a PreparedStatement
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
                int parameterIndex = 1;

                // Iterate schema
                for (Map.Entry<String, String> entry : schema.entrySet()) {

                    // compare with schema type, check if castable
                    String typeInSchema = entry.getValue();
                    Object value = data.get(entry.getKey());
                    if (value == null) {    // field not in input data
                        preparedStatement.setObject(parameterIndex, null);
                        parameterIndex++;
                        continue;
                    }
                    String typeReceived = value.getClass().getSimpleName();
                    //log.debug("Data field type: Received: {}, expected {}", typeReceived, typeInSchema);
                    try {
                        if (typeInSchema.equals("Double") || typeInSchema.equals("Float")) {

                            //Double v = (Double)entry.getValue();
                            BigDecimal decimalValue = new BigDecimal(value.toString());
                            decimalValue = decimalValue.setScale(floatScale, RoundingMode.HALF_UP);
                            //System.out.println(entry.getKey() + " is a float number: " + decimalValue);
                            preparedStatement.setObject(parameterIndex, decimalValue);

                        } else if (typeInSchema.equals("Integer")) {
                            Integer val = (Integer) value;
                            preparedStatement.setObject(parameterIndex, val);


                        } else if (typeInSchema.equals("String")) {
                            preparedStatement.setObject(parameterIndex, value.toString());

                        } else if (typeInSchema.equals("Timestamp")) {
                            Long v = (Long) value;
                            Date d = new Date(v);
                            preparedStatement.setObject(parameterIndex, d);

                        } else {    // String, Date (already formated)
                            preparedStatement.setObject(parameterIndex, value);  // already normalized
                        }

                    } catch (Exception e) {
                        log.trace("Field {}:{} have incorrect type. Received: {}, expected {}", entry.getKey(), value, typeReceived, typeInSchema);
                        preparedStatement.setObject(parameterIndex, null);
                    }
                    parameterIndex++;
                }

                // Execute the INSERT statement
                int rowsAffected = preparedStatement.executeUpdate();
                log.debug("Inserted " + rowsAffected + " row(s) into sensor_data table. in " + (System.currentTimeMillis()-start) +" ms");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertBatchIntoDatabase(LinkedList<Map<String, Object>> batch) {
        // TODO: assume this batch of same schema types!  (if different table then need to separate queue for each schemas)
        log.debug("Insert batch of " + batch.size());
        long start = System.currentTimeMillis();
        Map<String, Object> _data = batch.get(0);

        // Note: data already normalized
        // choose schema table from deviceToken
        String token = _data.get(Constant.DEVICE_TOKEN).toString();
        DeviceSchema dt = deviceSchemaService.findFromDeviceToken(token);
        if (dt==null) {
            log.error("Device Type not found");
        }
        log.debug("Get deviceType & schema " + dt.getName());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> schema;
        try {
            schema = mapper.readValue(dt.getDataSchema(), Map.class);
            //log.debug("Find schema from device token {}, schema: {}", token, schema);
        } catch (Exception e) {
            log.error("Exception when parsing schema: {}", e);    // should not happen! should validate schema before save
            return;
        }

        //try (Connection connection = DriverManager.getConnection(url, username, password)) {
        try (Connection connection = dataSource.getConnection()) {
            StringBuilder sqlBuilder = new StringBuilder("INSERT INTO " + dt.getTableName() + " (");
            StringBuilder valuesBuilder = new StringBuilder(") VALUES (");

            // Loop through the schema and build the SQL query dynamically
            for (Map.Entry<String, String> entry : schema.entrySet()) {
                sqlBuilder.append(entry.getKey()).append(",");
                valuesBuilder.append("?,");
            }

            // Remove the trailing commas and complete the SQL query
            sqlBuilder.setLength(sqlBuilder.length() - 1);
            valuesBuilder.setLength(valuesBuilder.length() - 1);
            sqlBuilder.append(valuesBuilder).append(")");

            //System.out.println("SQL: " + sqlBuilder);
            // Create a PreparedStatement
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {

                // Iterate in batch
                for (Map m: batch) {

                    int parameterIndex = 1;
                    // Iterate schema
                    for (Map.Entry<String, String> entry : schema.entrySet()) {

                        // compare with schema type, check if value is of correct format
                        String typeInSchema = entry.getValue();
                        Object value = m.get(entry.getKey());
                        if (value == null) {    // field not in input data
                            preparedStatement.setObject(parameterIndex, null);
                            parameterIndex++;
                            continue;
                        }
                        String typeReceived = value.getClass().getSimpleName();
                        //log.debug("Data field type: Received: {}, expected {}", typeReceived, typeInSchema);
                        try {
//                            if (!typeInSchema.equals(typeReceived)) {       // Bug:  Integer not saved if column is float although can parse!
//                                // Neu truong mis-match (thuong la loi "error" thi insert null
//                                log.trace("Field {}:{} have incorrect type. Received: {}, expected {} --> insert null", entry.getKey(), value, typeReceived, typeInSchema);
//                                preparedStatement.setObject(parameterIndex, null);  // already normalized
//                            }
                             if (typeInSchema.equals("Double") || typeInSchema.equals("Float")) {

                                //Double v = (Double)entry.getValue();
                                BigDecimal decimalValue = new BigDecimal(value.toString());
                                decimalValue = decimalValue.setScale(floatScale, RoundingMode.HALF_UP);
                                //System.out.println(entry.getKey() + " is a float number: " + decimalValue);
                                preparedStatement.setObject(parameterIndex, decimalValue);

                            } else if (typeInSchema.equals("Integer")) {
                                Integer val = (Integer) value;
                                preparedStatement.setObject(parameterIndex, val);

                            } else if (typeInSchema.equals("Timestamp")) {
                                Long v = (Long) value;
                                Date d = new Date(v);
                                preparedStatement.setObject(parameterIndex, d);

                            }  else  {    // String, Date (already formated, just save as is)
                                    preparedStatement.setObject(parameterIndex, value);  // already normalized
                            }
                        } catch (Exception e) {
                            log.trace("Can not parse: Received: {}, expected {},   Field {}:{} -> insert gia tri nulll", typeReceived, typeInSchema, entry.getKey(), e);
                            preparedStatement.setObject(parameterIndex, null);
                        }
                        parameterIndex++;
                    }
                    preparedStatement.addBatch();
                }

                // Execute the INSERT statement
                int[] batchResult = preparedStatement.executeBatch();
                // Handle batch results as needed
                int success = 0, fail = 0;
                for (int result : batchResult) {
                    if (result == PreparedStatement.EXECUTE_FAILED) {
                        fail++;
                    } else {
                        success++;
                    }
                }

                // Close the prepared statement
                preparedStatement.close();
                log.info("Insert {} items, {} OK, {} NOK, time {} ms", batchResult.length, success, fail, (System.currentTimeMillis()-start));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
