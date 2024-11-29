package org.seleniumbrain.lab.utility.json.validator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InDirectValidation {
    private String condition;
    private List<String> expressions;
}
