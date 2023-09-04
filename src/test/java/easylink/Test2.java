package easylink;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class Test2 {
    public static void main(String[] args) {
        String json = "{\"name\": \"John\", \"age\": 30, \"unknownField\": \"someValue\"}";

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse JSON into a Map<String, Object>
            Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);

            // Access known fields
            String name = (String) jsonMap.get("name");
            Integer age = (Integer) jsonMap.get("age");

            System.out.println("Name: " + name);
            System.out.println("Age: " + age);

            // Access unknown fields
            for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
                if (!entry.getKey().equals("name") && !entry.getKey().equals("age")) {
                    System.out.println("Unknown field: " + entry.getKey() + " - Value: " + entry.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
