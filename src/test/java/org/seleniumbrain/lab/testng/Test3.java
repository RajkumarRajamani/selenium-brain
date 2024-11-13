package org.seleniumbrain.lab.testng;

import lombok.Data;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;

public class Test3 {

    public static void main(String[] args) {

        String file = "src/test/resources/cucumber/input-files/sample.json";

        String json = JsonBuilder.getObjectBuilder().fromJsonFile(file).buildAsJsonNode().toPrettyString();

        System.out.println(json);

    }

    @Data
    public static class Exposure {
        private String exchangeRate;
    }
}
