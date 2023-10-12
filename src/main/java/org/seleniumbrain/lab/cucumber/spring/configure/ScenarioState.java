package org.seleniumbrain.lab.cucumber.spring.configure;

import io.cucumber.spring.ScenarioScope;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ScenarioScope
public class ScenarioState {
    private Map<StateKey, Object> state = new HashMap<>();

    @Data
    @Builder
    public static class CucumberStepLog {
        private byte[] img;
        private String imgCaption;
        private String textLog;
    }
}
