package org.seleniumbrain.lab.core.cucumber.spring.configure;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfigurationFactory {

    @Bean
    public SecretClient createAzureSecretClient() {
        return new SecretClientBuilder()
                .vaultUrl("https://qakeyvaulttest.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }
}
