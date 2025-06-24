package br.com.agibank.seguros.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientesApiConfig {

    @Value("${app.clientes.base-url}")
    private String clientesApiBaseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getClientesApiBaseUrl() {
        return clientesApiBaseUrl;
    }
}
