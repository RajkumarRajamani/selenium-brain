package org.seleniumbrain.lab.utility.json.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    private String field;
    private String value;
    private Operator operator;
    private ValueType type;
    private RequiredField requiredField;
}
