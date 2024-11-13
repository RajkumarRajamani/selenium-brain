package org.seleniumbrain.lab.utility.json.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {
    CONTAINS("contains"),
    EQUALS("equals"),
    EQUAL_IGNORE_CASE("equals ignore case"),
    NOT_EQUALS("not equals"),
    GREATER_THAN("greater than"),
    LESS_THAN("less than"),
    SELF("self");

    private final String value;

    Operator(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Operator fromValue(String value) {
        if (value == null || value.isBlank()) {
            return SELF; // Return a default value or handle it as needed
        }

        for (Operator status : Operator.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
