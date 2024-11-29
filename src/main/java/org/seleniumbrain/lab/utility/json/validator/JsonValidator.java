package org.seleniumbrain.lab.utility.json.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.seleniumbrain.lab.utility.FileUtils;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;
import org.seleniumbrain.lab.utility.json.core.NodeValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonValidator {

    private static final String fileName = "src/test/resources/cucumber/input-files/sample.json";
    private static final String complexJsonFile = "src/test/resources/cucumber/input-files/complexjson.json";
    String conditionFile = "src/test/resources/cucumber/input-files/conditions.json";

    static final String expectedConditionFile = "src/test/resources/cucumber/input-files/ExpectedConditions.json";

    public static void main(String[] args) throws JsonProcessingException {

//        getExpectedJson();

        compare(expectedConditionFile, complexJsonFile);
    }

    public static void getExpectedJson() {
        JsonBuilder builder = JsonBuilder.getObjectBuilder().fromJsonFile(complexJsonFile).build();

        String conditionFile = "src/test/resources/cucumber/input-files/conditions.json";
        String json = JsonBuilder.getArrayBuilder().fromJsonFile(conditionFile).buildAsJsonNode().toPrettyString();
        List<Condition> conditions = JsonBuilder.transformJsonToPojoList(json, Condition.class);

        // extract all json paths from parent json
        List<String> paths = JsonBuilder.printJsonPath(builder.buildAsJsonNode(), "", new ArrayList<>());

//        conditions.forEach(System.out::println);
        JsonBuilder objectBuilder = JsonBuilder.getObjectBuilder().withEmptyNode();

        for (Condition condition : conditions) {
            String conditionPath = condition.getField();
            String conditionValue = condition.getValue();
            Operator conditionOperator = condition.getOperator();
            ValueType conditionValueType = condition.getType();
            String requiredPathsRoot = condition.getRequiredField().getRoot();
            List<String> requiredPaths = condition.getRequiredField().getPaths();

            List<String> conditionPaths = paths.stream().filter(p -> p.replaceAll("\\[\\d+]", "").trim().equals(conditionPath)).toList();

            List<Condition> unavailablePaths = new ArrayList<>();

            if(conditionPaths.isEmpty())
                unavailablePaths.add(condition);
            else {
                if (conditionOperator.equals(Operator.SELF)) {
                    for (String condPath : conditionPaths) {
                        JsonNode condPathNode = builder.getNodeAt(condPath);
                        if (condPathNode.isArray()) {
                            for (int i = 0; i < condPathNode.size(); i++) {
                                if (condPathNode.get(i).isTextual())
                                    objectBuilder.append(condPath + "[" + i + "]", condPathNode.get(i).textValue());
                                else
                                    objectBuilder.append(condPath, condPathNode.toPrettyString(), NodeValueType.ARRAY_STYLE_JSON.getType());
                            }
                        } else if (condPathNode.isObject()) {
                            objectBuilder.append(condPath, condPathNode.toPrettyString(), NodeValueType.OBJECT_STYLE_JSON.getType());
                        } else {
                            String value = builder.getNodeAt(condPath).asText();
                            objectBuilder.append(condPath, value);
                        }
                    }
                } else if (condition.getOperator().equals(Operator.EQUALS)) {
                    for (String condPath : conditionPaths) {
                        String condPathVal = builder.getNodeAt(condPath).asText();
                        if (condPathVal.equals(conditionValue)) {
                            int length = requiredPathsRoot.split("\\.").length;
                            String[] array = condPath.split("\\.");
                            String rootNodePath = Arrays.stream(array, 0, length)
                                    .collect(Collectors.joining("."));
                            List<String> revisedRequiredPaths = paths.stream()
                                    .filter(p -> p.startsWith(rootNodePath))
                                    .filter(p -> {
                                        String s = p.replace(rootNodePath + ".", "").replaceAll("\\[\\d+]", "").trim().trim();
                                        return requiredPaths.contains(s);
                                    })
                                    .toList();

                            for (String revisedRequiredPath : revisedRequiredPaths) {
                                JsonNode revisedRequiredNode = builder.getNodeAt(revisedRequiredPath);
                                if (revisedRequiredNode.isArray()) {
                                    for (int i = 0; i < revisedRequiredNode.size(); i++) {
                                        if (revisedRequiredNode.get(i).isTextual())
                                            objectBuilder.append(revisedRequiredPath + "[" + i + "]", revisedRequiredNode.get(i).textValue());
                                        else
                                            objectBuilder.append(revisedRequiredPath, revisedRequiredNode.toPrettyString(), NodeValueType.ARRAY_STYLE_JSON.getType());
                                    }
                                } else if (revisedRequiredNode.isObject()) {
                                    objectBuilder.append(revisedRequiredPath, revisedRequiredNode.toPrettyString(), NodeValueType.OBJECT_STYLE_JSON.getType());
                                } else {
                                    String value = builder.getNodeAt(revisedRequiredPath).asText();
                                    objectBuilder.append(revisedRequiredPath, value);
                                }
                            }
                        }
                    }
                }
            }

            if(!unavailablePaths.isEmpty())
                System.out.println("Unavailable Fields : " + unavailablePaths);

        }

        String finalJson = objectBuilder.build().toPrettyString();
//        System.out.println(finalJson);
        new FileUtils().write("src/test/resources/cucumber/input-files/finalJson.json", finalJson);
    }

    public static void validate(String conditionFile, String actualJsonToCompare) {
        JsonBuilder actualJson_builder = JsonBuilder.getObjectBuilder().fromJsonFile(actualJsonToCompare).build();

        String json = JsonBuilder.getArrayBuilder().fromJsonFile(conditionFile).buildAsJsonNode().toPrettyString();
        List<Condition> conditions = JsonBuilder.transformJsonToPojoList(json, Condition.class);

        // extract all json paths from parent json
        List<String> paths = JsonBuilder.printJsonPath(actualJson_builder.buildAsJsonNode(), "", new ArrayList<>());

//        conditions.forEach(System.out::println);
        JsonBuilder objectBuilder = JsonBuilder.getObjectBuilder().withEmptyNode();

        for (Condition condition : conditions) {
            String conditionPath = condition.getField();
            String conditionValue = condition.getValue();
            Operator conditionOperator = condition.getOperator();
            ValueType conditionValueType = condition.getType();
            String requiredPathsRoot = condition.getRequiredField().getRoot();
            List<String> requiredPaths = condition.getRequiredField().getPaths();

            List<String> conditionPaths = paths.stream().filter(p -> p.replaceAll("\\[\\d+]", "").trim().equals(conditionPath)).toList();

            List<Condition> unavailablePaths = new ArrayList<>();

            if(conditionPaths.isEmpty())
                unavailablePaths.add(condition);
            else {
                if (conditionOperator.equals(Operator.SELF)) {
                    for (String condPath : conditionPaths) {
                        JsonNode condPathNode = actualJson_builder.getNodeAt(condPath);
                        if (condPathNode.isArray()) {
                            for (int i = 0; i < condPathNode.size(); i++) {
                                if (condPathNode.get(i).isTextual())
                                    objectBuilder.append(condPath + "[" + i + "]", condPathNode.get(i).textValue());
                                else
                                    objectBuilder.append(condPath, condPathNode.toPrettyString(), NodeValueType.ARRAY_STYLE_JSON.getType());
                            }
                        } else if (condPathNode.isObject()) {
                            objectBuilder.append(condPath, condPathNode.toPrettyString(), NodeValueType.OBJECT_STYLE_JSON.getType());
                        } else {
                            String value = actualJson_builder.getNodeAt(condPath).asText();
                            objectBuilder.append(condPath, value);
                        }
                    }
                } else if (condition.getOperator().equals(Operator.EQUALS)) {
                    for (String condPath : conditionPaths) {
                        String condPathVal = actualJson_builder.getNodeAt(condPath).asText();
                        if (condPathVal.equals(conditionValue)) {
                            int length = requiredPathsRoot.split("\\.").length;
                            String[] array = condPath.split("\\.");
                            String rootNodePath = Arrays.stream(array, 0, length)
                                    .collect(Collectors.joining("."));
                            List<String> revisedRequiredPaths = paths.stream()
                                    .filter(p -> p.startsWith(rootNodePath))
                                    .filter(p -> {
                                        String s = p.replace(rootNodePath + ".", "").replaceAll("\\[\\d+]", "").trim().trim();
                                        return requiredPaths.contains(s);
                                    })
                                    .toList();

                            for (String revisedRequiredPath : revisedRequiredPaths) {
                                JsonNode revisedRequiredNode = actualJson_builder.getNodeAt(revisedRequiredPath);
                                if (revisedRequiredNode.isArray()) {
                                    for (int i = 0; i < revisedRequiredNode.size(); i++) {
                                        if (revisedRequiredNode.get(i).isTextual())
                                            objectBuilder.append(revisedRequiredPath + "[" + i + "]", revisedRequiredNode.get(i).textValue());
                                        else
                                            objectBuilder.append(revisedRequiredPath, revisedRequiredNode.toPrettyString(), NodeValueType.ARRAY_STYLE_JSON.getType());
                                    }
                                } else if (revisedRequiredNode.isObject()) {
                                    objectBuilder.append(revisedRequiredPath, revisedRequiredNode.toPrettyString(), NodeValueType.OBJECT_STYLE_JSON.getType());
                                } else {
                                    String value = actualJson_builder.getNodeAt(revisedRequiredPath).asText();
                                    objectBuilder.append(revisedRequiredPath, value);
                                }
                            }
                        }
                    }
                }
            }

            if(!unavailablePaths.isEmpty())
                System.out.println("Unavailable Fields : " + unavailablePaths);

        }

        String finalJson = objectBuilder.build().toPrettyString();
//        System.out.println(finalJson);
        new FileUtils().write("src/test/resources/cucumber/input-files/finalJson.json", finalJson);
    }

    public static void compare(String conditionFile, String actualJson) {

        JsonBuilder actualJson_builder = JsonBuilder.getObjectBuilder().fromJsonFile(actualJson).build();

        String json = JsonBuilder.getArrayBuilder().fromJsonFile(conditionFile).buildAsJsonNode().toPrettyString();
        List<Condition> conditions = JsonBuilder.transformJsonToPojoList(json, Condition.class);


    }
}
