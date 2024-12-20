package org.seleniumbrain.lab.utility.json.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.*;
import org.seleniumbrain.lab.utility.FileUtils;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;
import org.seleniumbrain.lab.utility.json.core.NodeValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonValidator {

    private static final String fileName = "src/test/resources/cucumber/input-files/sample.json";
    private static final String complexJsonFile = "src/test/resources/cucumber/input-files/complexjson2.json";
    String conditionFile = "src/test/resources/cucumber/input-files/conditions.json";

    static final String ruleBookFile = "src/test/resources/cucumber/input-files/rule-book.json";

    public static void main(String[] args) throws JsonProcessingException {

        JsonBuilder actualJson_builder = JsonBuilder.getArrayBuilder().fromJsonFile(complexJsonFile).build();
        String actualJson = actualJson_builder.toPrettyString();

        // Set up configuration for JsonPath Expression
        Configuration configuration = Configuration.defaultConfiguration()
                .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                .addOptions(Option.ALWAYS_RETURN_LIST);

        // read the entire actual JSON and store as ReadContext by using the above configurations
        ReadContext context = JsonPath.using(configuration).parse(actualJson);

        // $[?(@.attributes[?(@.type == 'A') && @.attributes[?(@.value == 15)])]]
//        List<String> res = context.read("$.friends[?(@.age == 28 && @.pets[?(@.species == /^cowq$/)])]");
//        @.pets[?(@.species == /^cowq$/)]
//        List<String> res = context.read("$.friends[?(@.age == 28) && @.friends[?(@.pets[?(@.species == 'cat')])]]");
        List<Object> res = context.read("$[?(@.friends[?(@.age == 27 && @.pets[?(@.species == 'cat')])])]");
        System.out.println(JsonBuilder.transformPojoToJsonNode(res).toPrettyString());

//        compare(ruleBookFile, complexJsonFile);
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

            if (conditionPaths.isEmpty())
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

            if (!unavailablePaths.isEmpty())
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

            if (conditionPaths.isEmpty())
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

            if (!unavailablePaths.isEmpty())
                System.out.println("Unavailable Fields : " + unavailablePaths);

        }

        String finalJson = objectBuilder.build().toPrettyString();
//        System.out.println(finalJson);
        new FileUtils().write("src/test/resources/cucumber/input-files/finalJson.json", finalJson);
    }


    /**
     * <pre>
     * Compares the given JSON file against a set of validation rules specified in a rule book file.
     *
     * This method reads the rules defined in the `ruleBookFile`, which specifies JSONPath expressions
     * and validation criteria. It then applies these rules to the content of the `actualJsonFile` and
     * evaluates whether the values meet the specified conditions. The result is a list of error messages
     * for all validation failures. If all validations pass, the returned list will be empty.
     *
     * **RuleBook File Format:**
     * The `ruleBookFile` should be a JSON file containing validation rules. Each rule should specify:
     * - `pathExpression`: A JSONPath expression identifying the element(s) to validate.
     * - `condition`: The expected condition or value for the element(s).
     * - `description`: A brief description of the validation.
     *
     * For JSONPath expressions, refer to the [JSONPath documentation](https://github.com/json-path/JsonPath/blob/master/README.md).
     *
     * **Example Usage:**
     * Given:
     * 1. RuleBook file:
     * ```json
     * [
     *   {
     *     "description": "name node",
     *     "dirCheck": true,
     *     "dirValidation": {
     *       "expressions": [
     *         "$[?(@.name == 'Alice')]"
     *       ]
     *     },
     *     "indValidation": {
     *       "condition": "",
     *       "expressions": []
     *     }
     *   },
     *   {
     *     "description": "name1 node",
     *     "dirCheck": true,
     *     "dirValidation": {
     *       "expressions": [
     *         "$[?(@.name1 == 'Alice')]"
     *       ]
     *     },
     *     "indValidation": {
     *       "condition": "",
     *       "expressions": []
     *     }
     *   },
     *   {
     *     "description": "friends[].hobbies node",
     *     "dirCheck": false,
     *     "dirValidation": {
     *       "expressions": []
     *     },
     *     "indValidation": {
     *       "condition": "$.friends[?(@.age == 32)]",
     *       "expressions": [
     *         "$[*].hobbies[?(@ == 'gaming')]",
     *         "$[*].hobbies[?(@ == 'writting')]"
     *       ]
     *     }
     *   }
     * ]
     * ```
     * 2. Actual JSON file:
     * ```json
     * {
     *   "name": "Alice",
     *   "friends": [
     *     {
     *       "name": "Bob",
     *       "age": 28,
     *       "hobbies": [
     *         "gaming",
     *         "music"
     *       ]
     *     },
     *     {
     *       "name": "Charlie",
     *       "age": 32,
     *       "hobbies": [
     *         "paintting",
     *         "writting"
     *       ],
     *       "pets": [
     *         {
     *           "name": "Fluffy",
     *           "species": "cat"
     *         },
     *         {
     *           "name": "Buddy",
     *           "species": "dog"
     *         }
     *       ]
     *     }
     *   ]
     * }
     * ```
     * 3. Output in returned List<String> object
     * ```java
     * name1 node => $[?(@.name1 == 'Alice')]
     * friends[].hobbies node => $[*].hobbies[?(@ == 'gaming')]
     * ```
     * This method will return an empty list since both validations pass.
     * </pre>
     *
     * @param ruleBookFile   the file path to the JSON containing validation rules
     * @param actualJsonFile the file path to the JSON to be validated
     * @return a list of error messages for failed validations, or an empty list if all validations pass
     * @see <a href="https://github.com/json-path/JsonPath/blob/master/README.md">JSONPath Documentation</a>
     */
    public static List<String> compare(String ruleBookFile, String actualJsonFile) {

        // Read the actual JSON file to run rules against - for validation
        JsonBuilder actualJson_builder = JsonBuilder.getObjectBuilder().fromJsonFile(actualJsonFile).build();
        String actualJson = actualJson_builder.toPrettyString();

        // Read Rules from a rule-book.json file
        String json = JsonBuilder.getArrayBuilder().fromJsonFile(ruleBookFile).buildAsJsonNode().toPrettyString();
        List<RuleBook> rules = JsonBuilder.transformJsonToPojoList(json, RuleBook.class);

        // Set up configuration for JsonPath Expression
        Configuration configuration = Configuration.defaultConfiguration()
                .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                .addOptions(Option.ALWAYS_RETURN_LIST);

        // read the entire actual JSON and store as ReadContext by using the above configurations
        ReadContext context = JsonPath.using(configuration).parse(actualJson_builder.toPrettyString());

        // List of rules that failed
        List<String> failedRules = new ArrayList<>();

        for (RuleBook rule : rules) {
            if (rule.isDirCheck()) {
                List<String> jsonPathExpressions = rule.getDirValidation().getExpressions();

                for (String jsonPathExpression : jsonPathExpressions) {
                    List<Object> result = context.read(jsonPathExpression);

                    if (result.isEmpty()) // result's size > 0, then a match is found in actual JSON. Otherwise, failed.
                        failedRules.add(String.join(" => ", rule.getDescription(), jsonPathExpression));
                }
            } else {
                InDirectValidation inDirectValidation = rule.getIndValidation();
                String conditionPathExpression = inDirectValidation.getCondition();
                List<Object> result = context.read(conditionPathExpression);

                if (!result.isEmpty()) {
                    String satisfiedJsonNode = JsonBuilder.transformPojoToJsonNode(result).toPrettyString();

                    // if pathExpressions are given, then
                    if (!inDirectValidation.getExpressions().isEmpty()) {
                        ReadContext innerContext = JsonPath.using(configuration).parse(satisfiedJsonNode);

                        for (String innerPathExpression : inDirectValidation.getExpressions()) {
                            List<Object> innerResult = innerContext.read(innerPathExpression);
                            if (innerResult.isEmpty())
                                failedRules.add(String.join(" => ", rule.getDescription(), innerPathExpression));
                        }
                    }
                } else {
                    failedRules.add(rule.getDescription());
                }
            }
        }
        failedRules.forEach(System.out::println);
        return failedRules;
    }
}
