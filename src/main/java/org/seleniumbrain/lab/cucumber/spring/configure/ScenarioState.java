package org.seleniumbrain.lab.cucumber.spring.configure;

import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@ScenarioScope
public class ScenarioState {
    private Map<Enum<?>, String> cacheText = new HashMap<>();
    private Map<Enum<?>, Object> cacheObject = new HashMap<>();
    private List<CucumberStepLog> stepLogs = new ArrayList<>();
}
