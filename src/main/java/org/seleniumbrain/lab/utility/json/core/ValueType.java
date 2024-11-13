package org.seleniumbrain.lab.utility.json.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ValueType {
    INTEGER("integer"),
    DECIMAL("decimal"),
    STRING("string"),
    BOOLEAN("boolean");

    private final String value;

    ValueType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ValueType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return STRING; // Return a default value or handle it as needed
        }

        for (ValueType status : ValueType.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
