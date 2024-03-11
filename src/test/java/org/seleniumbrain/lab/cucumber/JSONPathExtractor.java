package org.seleniumbrain.lab.cucumber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;

public class JSONPathExtractor {

    public static void main(String[] args) {
        String jsonString = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"address\": {\n" +
                "    \"city\": \"New York\",\n" +
                "    \"zipcode\": \"10001\"\n" +
                "  },\n" +
                "  \"contacts\": [\n" +
                "    {\n" +
                "      \"type\": \"email\",\n" +
                "      \"value\": \"john@example.com\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"phone\",\n" +
                "      \"value\": \"1234567890\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);

            extractJSONPath(rootNode, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void extractJSONPath(JsonNode node, String parentPath) {
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = node.get(fieldName);

                String currentPath = parentPath.isEmpty() ? fieldName : parentPath + "." + fieldName;
                System.out.println(currentPath);

                extractJSONPath(childNode, currentPath);
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                JsonNode arrayNode = node.get(i);
                extractJSONPath(arrayNode, parentPath + "[" + i + "]");
            }
        }
    }
}
