package org.seleniumbrain.lab.core.config.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * It contains configuration details of Application Under Test (AUT)
 */
@Data
public class AUTConfiguration {

    private UiConfig uiConfig;
    private ApiConfig apiConfig;
    private Switches switches;

    @Data
    public static class UiConfig {
        private String url;
    }

    @Data
    public static class ApiConfig {
        private Map<String, String> host = new HashMap<>();
        private Map<String, String> endPoints = new HashMap<>();

        private Token tokens;
    }

    @Data
    public static class Token {
        private TokenConfig tokenA = new TokenConfig();
    }

    @Data
    public static class TokenConfig {
        private String tokenUrl;
        private String grantType;
        private String clientId;
        private String secret;
        private String scope;
        private String resource;
        private String redirectUrl;
    }

    @Data
    public static class Switches {
        private boolean flagA;
        private boolean flagB;
        private boolean generateDocsBeforeExecution;
        private boolean deleteRecordsEndExecution;
    }

}
