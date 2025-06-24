package br.com.agibank.seguros.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SegurosConfig.ClientesApiProperties.class)
public class SegurosConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "clientes")
    public ClientesApiProperties clientesApiProperties() {
        return new ClientesApiProperties();
    }
    
    @lombok.Data
    @ConfigurationProperties(prefix = "clientes")
    public static class ClientesApiProperties {
        private String baseUrl;
    }
}
