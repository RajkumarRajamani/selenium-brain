package org.seleniumbrain.lab.cucumber.hooks;

import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LobSynchronizer {

    private LobSynchronizer() {}

    private static class SingletonHolder {
        private static final LobSynchronizer singleton = new LobSynchronizer();
    }

    public static LobSynchronizer getInstance() {
        return SingletonHolder.singleton;
    }

    private static final Map<String, ScenarioStatus> activeScenarios = new ConcurrentHashMap<>();

    public synchronized Map<String, ScenarioStatus> getActiveScenarios() {
        return activeScenarios;
    }

    public synchronized void storeScenarioStatusAsLive(String scenarioId) {
        activeScenarios.put(scenarioId, ScenarioStatus.LIVE);
    }

    public synchronized void closeScenarioStatus(String scenarioId, Scenario scenario) {
        activeScenarios.entrySet()
                .removeIf(entry -> entry.getKey().equalsIgnoreCase(scenarioId));
        activeScenarios.put(scenario.getStatus() + "-" + scenarioId, ScenarioStatus.CLOSED);
    }

    public synchronized boolean canLobBeSwitchedTo(String lob, String scenarioId) {
        log.info("Trying for scenario : " + scenarioId);
        String printFormat = "%-10s %-500s";
        activeScenarios.forEach((key, value) -> {
            log.info(String.format(printFormat + "%n", value, key));
        });

        if(activeScenarios.isEmpty()) {
            storeScenarioStatusAsLive(scenarioId);
            return true;
        } else {
            switch (lob) {
                case "Terrorism" -> {
                    boolean isAnyTerrorismScenarioActive = activeScenarios.entrySet().stream()
                            .filter(entry -> entry.getKey().split("\\|")[0].contains("Terrorism"))
                            .anyMatch(entry -> entry.getValue().equals(ScenarioStatus.LIVE));
                    boolean isOtherLobScenarioClosed = activeScenarios.entrySet().stream()
                            .filter(entry -> !entry.getKey().split("\\|")[0].contains("Terrorism"))
                            .allMatch(entry -> entry.getValue().equals(ScenarioStatus.CLOSED));
                    log.info("isAnyTerrorismScenarioActive?/isOtherLobScenarioClosed? : " + isAnyTerrorismScenarioActive + "/" + isOtherLobScenarioClosed + "\n");
                    if(isAnyTerrorismScenarioActive || isOtherLobScenarioClosed) {
                        storeScenarioStatusAsLive(scenarioId);
                        return true;
                    } else {
                        return false;
                    }
                }

                case "Casualty" -> {
                    boolean isAnyCasualtyScenarioActive = activeScenarios.entrySet().stream()
                            .filter(entry -> entry.getKey().split("\\|")[0].contains("Casualty"))
                            .anyMatch(entry -> entry.getValue().equals(ScenarioStatus.LIVE));
                    boolean isOtherLobScenarioClosed = activeScenarios.entrySet().stream()
                            .filter(entry -> !entry.getKey().split("\\|")[0].contains("Casualty"))
                            .allMatch(entry -> entry.getValue().equals(ScenarioStatus.CLOSED));
                    log.info("isAnyCasualtyScenarioActive?/isOtherLobScenarioClosed? : " + isAnyCasualtyScenarioActive + "/" + isOtherLobScenarioClosed + "\n");
                    if(isAnyCasualtyScenarioActive || isOtherLobScenarioClosed) {
                        storeScenarioStatusAsLive(scenarioId);
                        return true;
                    } else {
                        return false;
                    }
                }

                case "Prcb" -> {
                    boolean isAnyPrcbScenarioActive = activeScenarios.entrySet().stream()
                            .filter(entry -> entry.getKey().split("\\|")[0].contains("Prcb"))
                            .anyMatch(entry -> entry.getValue().equals(ScenarioStatus.LIVE));
                    boolean isOtherLobScenarioClosed = activeScenarios.entrySet().stream()
                            .filter(entry -> !entry.getKey().split("\\|")[0].contains("Prcb"))
                            .allMatch(entry -> entry.getValue().equals(ScenarioStatus.CLOSED));
                    log.info("isAnyPrcbScenarioActive?/isOtherLobScenarioClosed? : " + isAnyPrcbScenarioActive + "/" + isOtherLobScenarioClosed + "\n");
                    if(isAnyPrcbScenarioActive || isOtherLobScenarioClosed) {
                        storeScenarioStatusAsLive(scenarioId);
                        return true;
                    } else {
                        return false;
                    }
                }

                default -> {
                    return true;
                }
            }
        }
    }

    public synchronized String getLobOfScenario(Scenario scenario) {
        if(!scenario.getSourceTagNames().stream().filter(tag -> tag.contains("@Terrorism")).findFirst().orElse("").isBlank()) {
            return "Terrorism";
        } else if(!scenario.getSourceTagNames().stream().filter(tag -> tag.contains("@Casualty")).findFirst().orElse("").isBlank()) {
            return "Casualty";
        } else if(!scenario.getSourceTagNames().stream().filter(tag -> tag.contains("@Prcb")).findFirst().orElse("").isBlank()) {
            return "Prcb";
        } else {
            log.info("Taking default LOB as scenario does not contain any LOB tag name ; Scenario : " + scenario.getName());
            return "Terrorism";
        }
    }
}
