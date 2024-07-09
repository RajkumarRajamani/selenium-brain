package org.seleniumbrain.lab.cucumber;

import lombok.Data;
import org.seleniumbrain.lab.utility.json.core.JsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class JsonTest {

    public static void main(String[] args) {
        String json = "[1, 2, 3]";

        List<Integer> versions = JsonBuilder.transformJsonToPojoList(json, Integer.class);

        System.out.println(versions);
    }

    @Data
    public static class Versions {
        private List<Integer> availableVersions = new ArrayList<>();
    }

}
