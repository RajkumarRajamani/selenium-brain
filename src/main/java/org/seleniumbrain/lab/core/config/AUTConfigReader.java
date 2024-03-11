package org.seleniumbrain.lab.core.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.seleniumbrain.lab.core.config.pojo.AUTConfig;
import org.seleniumbrain.lab.core.exception.ConfigurationReadException;

import java.io.IOException;

/**
 * @implNote It reads the configurations of Application Under Test (AUT).
 * Maintain all required configurations here such as url, api end-points, token generator details, etc.
 * It reads the application properties from environment-specific YML files
 * as we may have to maintain different endpoints for different non-prod environments.
 *
 * Ensure that YML configurations are following the structure as in the {@link AUTConfig}
 *
 */
public class AUTConfigReader {

    private static final String APPLICATION_UNDER_TEST_CONFIG = String.join("/",  "", "configs", "aut-configs",
            SeleniumConfigReader.getConfigs().getTest().getApp().getEnvironment() + ".yml");
    private static final AUTConfig autConfig;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            autConfig = mapper.readValue(
                    SeleniumConfigReader.class.getResourceAsStream(APPLICATION_UNDER_TEST_CONFIG),
                    AUTConfig.class);
        } catch (IOException e) {
            throw new ConfigurationReadException("Exception while reading AUT(Application Under Test) Configuration.", e);
        }
    }

    public static AUTConfig get() {
        return autConfig;
    }
}
