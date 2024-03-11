package org.seleniumbrain.lab.core.selenium.validator;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class ValidatorOutput {
    private boolean passed;
    private Set<String> errors;

    public String getErrorsAsText() {
        return String.join(" ; ", errors);
    }
}
