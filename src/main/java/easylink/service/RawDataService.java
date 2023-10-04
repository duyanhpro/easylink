package easylink.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * Insert raw data into Starrock
 */
@Service
public class RawDataService {

    static Logger log = LoggerFactory.getLogger(RawDataService.class);

    //String jdbcUrl = "jdbc:mariadb://localhost:3306/easylink?useTimezone=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    String jdbcUrl = "jdbc:mysql://113.190.243.86:21930/easylink?useTimezone=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";      // starrocks
    String username = "root";
    String password = "";

    String tableName = "sensor_data2";

    public void insertData(String deviceToken, Map<String, Object> data) {
        log.debug("InsertData");
        // TODO: choose schema table from deviceToken

        // Insert into starrock table:
        data.put("event_time", new Date());
        data.put("device_token", deviceToken);
        data.remove("deviceToken");

        // TODO: handle field with value = "error" (can't insert because wrong data type)
        // TODO: compare type with Schema and
//SET enable_insert_strict = {false};
        // Establish a database connection
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            StringBuilder sqlBuilder = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder valuesBuilder = new StringBuilder(") VALUES (");

            // Loop through the HashMap and build the SQL query dynamically
            // TODO: skip unknown fields or set null fields with wrong data types
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                // Check if the value is a String and matches "error"
                System.out.println(entry.getKey() + ": " + entry.getValue().getClass().getName());

                if (entry.getValue() instanceof String && ((String) entry.getValue()).equalsIgnoreCase("error")) {
                    System.out.println(entry.getKey() + " is an error.");
                    continue;
                    // Check if the value is a Float
                }
                if (entry.getValue().equals("error"))
                    continue;

                String columnName = entry.getKey();


                sqlBuilder.append(columnName).append(",");
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

                // Set the values from the HashMap
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    if (entry.getValue().equals("error"))
                        continue;
                    //System.out.println(entry.getKey() +":" + entry.getValue());
                    Object columnValue = entry.getValue();
//                    BigDecimal bigDecimal = new BigDecimal((Double)columnValue);
//                    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);


                    if (entry.getValue() instanceof Double || entry.getValue() instanceof Float) {
                        //Double floatValue = (Double) entry.getValue();
                        BigDecimal decimalValue = new BigDecimal(entry.getValue().toString());
                        decimalValue = decimalValue.setScale(2, RoundingMode.HALF_UP);
                        System.out.println(entry.getKey() + " is a float number: " + decimalValue);

                        // Check if the value is a Number (Integer, Double, etc.)
                        preparedStatement.setObject(parameterIndex, decimalValue);
                    } else {
                        preparedStatement.setObject(parameterIndex, entry.getValue());
                    }

                    //preparedStatement.setObject(parameterIndex, bigDecimal.doubleValue());
                    parameterIndex++;
                }

                // Execute the INSERT statement
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Inserted " + rowsAffected + " row(s) into sensor_data table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
