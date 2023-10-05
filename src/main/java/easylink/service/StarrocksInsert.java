package easylink.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import easylink.common.Constant;
import easylink.entity.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.PreDestroy;

@Service
public class StarrocksInsert implements IngestDataService {

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
    DeviceTypeService deviceTypeService;

    // Create a batch for insertion
    LinkedList<Map<String, Object>> batch = new LinkedList<>();
    long first = 0;

    @Override
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
        dataSource = new HikariDataSource(config);

        log.info("Create thread to ingest data");
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    Map<String, Object> m = messageQueue.poll();
                    if (m == null) {
                        try {
                            Thread.sleep(QUEUE_POLL_TIME_MS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

//                    insertOne(m);

                    // add message to batch
                    if (batch.isEmpty())
                        first = System.currentTimeMillis();
                    batch.add(m);

                    // Check if it is time to insert
                    if ((batch.size() >= batchSize) || ((System.currentTimeMillis() - first) >= (batchMaxWaitTime *1000))) {
                        insertBatchIntoDatabase(batch);
                        // reset
                        batch.clear();
                    }
                }
            }
        }).start();
    }

    @PreDestroy
    public void closeDataSource() {
        log.info("Clean up Starrocks connection pool");
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
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
        DeviceType dt = deviceTypeService.findFromDeviceToken(token);
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
        // TODO: assume this batch of same schema types!  (will separate queue for each device types
        log.info("Insert batch of " + batch.size());
        long start = System.currentTimeMillis();
        Map<String, Object> _data = batch.get(0);

        // Note: data already normalized
        // choose schema table from deviceToken
        String token = _data.get(Constant.DEVICE_TOKEN).toString();
        DeviceType dt = deviceTypeService.findFromDeviceToken(token);
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

                            } else {    // String, Date (already formated, just save as is)
                                preparedStatement.setObject(parameterIndex, value);  // already normalized
                            }

                        } catch (Exception e) {
                            log.trace("Field {}:{} have incorrect type. Received: {}, expected {}", entry.getKey(), value, typeReceived, typeInSchema);
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
