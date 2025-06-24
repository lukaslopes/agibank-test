package br.com.agibank.seguros.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ClientesApiConfig {

    @Value("${app.clientes.base-url}")
    private String clientesApiBaseUrl;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos de timeout de conexão
        factory.setReadTimeout(5000);    // 5 segundos de timeout de leitura
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        // Adiciona logging das requisições
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add((request, body, execution) -> {
            long startTime = System.currentTimeMillis();
            log.debug("Iniciando chamada para: {} {}", request.getMethod(), request.getURI());
            try {
                return execution.execute(request, body);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                log.debug("Chamada finalizada: {} {} - {} ms", request.getMethod(), request.getURI(), duration);
            }
        });
        restTemplate.setInterceptors(interceptors);
        
        return restTemplate;
    }

    public String getClientesApiBaseUrl() {
        return clientesApiBaseUrl;
    }
}
