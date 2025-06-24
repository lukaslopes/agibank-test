package br.com.agibank.seguros.infrastructure.client;

import br.com.agibank.seguros.infrastructure.client.dto.ClienteResponse;
import br.com.agibank.seguros.infrastructure.config.ClientesApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientesApiClient {

    private static final String CLIENTES_ENDPOINT = "/cpf/{cpf}";
    
    private final RestTemplate restTemplate;
    private final ClientesApiConfig clientesApiConfig;
    
    public ClienteResponse buscarClientePorCpf(String cpf) {
        try {
            String url = clientesApiConfig.getClientesApiBaseUrl()  + CLIENTES_ENDPOINT;
            ResponseEntity<ClienteResponse> response = restTemplate.getForEntity(
                url, 
                ClienteResponse.class, 
                cpf);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            return null;
        } catch (HttpClientErrorException.NotFound ex) {
            log.info("Cliente não encontrado para o CPF: {}", cpf);
            return null;
        } catch (Exception ex) {
            log.error("Erro ao buscar cliente por CPF: " + cpf, ex);
            throw new RuntimeException("Erro ao consultar serviço de clientes", ex);
        }
    }
    
    public boolean clienteExiste(String cpf) {
        return buscarClientePorCpf(cpf) != null;
    }
}
