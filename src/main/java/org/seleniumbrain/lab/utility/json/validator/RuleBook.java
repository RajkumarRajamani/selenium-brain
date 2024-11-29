package org.seleniumbrain.lab.utility.json.validator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleBook {
    private String description;
    private boolean dirCheck;
    private DirectValidation dirValidation;
    private InDirectValidation indValidation;
}
