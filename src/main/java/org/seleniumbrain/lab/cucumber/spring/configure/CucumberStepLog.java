package org.seleniumbrain.lab.cucumber.spring.configure;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CucumberStepLog {
    private byte[] img;
    private String caption;
    private String textLog;
}