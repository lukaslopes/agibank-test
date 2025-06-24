package br.com.agibank.seguros.infrastructure.client;

import br.com.agibank.seguros.infrastructure.client.dto.ClienteResponse;
import br.com.agibank.seguros.infrastructure.config.ClientesApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientesApiClient {

    private static final String CLIENTES_ENDPOINT = "/cpf/{cpf}";
    
    private final RestTemplate restTemplate;
    private final ClientesApiConfig clientesApiConfig;
    
    public ClienteResponse buscarClientePorCpf(String cpf) {
        String url = clientesApiConfig.getClientesApiBaseUrl() + CLIENTES_ENDPOINT;
        
        try {
            ResponseEntity<ClienteResponse> response = restTemplate.getForEntity(
                url, 
                ClienteResponse.class, 
                cpf);
        
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            
            log.warn("Resposta inesperada do serviço de clientes. Status: {}", response.getStatusCode());
            return null;
            
        } catch (Exception e) {
            log.error("Erro ao buscar cliente por CPF: {}", e.getMessage());
            throw e; // Relança para o mecanismo de retry
        }
    }
    
    @Retryable(
        value = { ResourceAccessException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public boolean clienteExiste(String cpf) {
        log.info("Verificando se o cliente com CPF {} existe", cpf);
        try {
            ClienteResponse cliente = buscarClientePorCpf(cpf);
            boolean existe = cliente != null;
            log.info("Cliente com CPF {} {}", cpf, existe ? "encontrado" : "não encontrado");
            return existe;
        } catch (Exception e) {
            log.error("Erro ao verificar existência do cliente com CPF {}: {}", cpf, e.getMessage());
            throw e;
        }
    }
    
    @Recover
    public boolean recoverClienteExiste(ResourceAccessException ex, String cpf) {
        log.error("Falha ao verificar cliente após todas as tentativas. CPF: {}", cpf, ex);
        return false; // Retorna false em caso de falha na comunicação
    }
}
