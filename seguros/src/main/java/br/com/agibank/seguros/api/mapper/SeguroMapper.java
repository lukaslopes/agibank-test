package br.com.agibank.seguros.api.mapper;

import br.com.agibank.seguros.api.dto.request.ContratarSeguroRequest;
import br.com.agibank.seguros.api.dto.response.ContratacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoTodosSegurosResponse;
import br.com.agibank.seguros.domain.model.Seguro;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper responsável por converter entre entidades de domínio e DTOs do módulo de seguros.
 */
@Component
public class SeguroMapper {
    
    /**
     * Converte um DTO de requisição para o domínio Seguro.
     *
     * @param request DTO de requisição de contratação de seguro
     * @return Instância de Seguro
     */
    public Seguro toModel(ContratarSeguroRequest request) {
        if (request == null) {
            return null;
        }
        
        return Seguro.builder()
                .cpfCliente(request.getCpf())
                .tipo(request.getTipo())
                .clienteEncontrado(true) // Assume que se chegou até aqui, o cliente existe
                .build();
    }
    
    /**
     * Converte um domínio Seguro para DTO de resposta de contratação.
     *
     * @param seguro Entidade de domínio Seguro
     * @return DTO de resposta de contratação
     */
    public ContratacaoSeguroResponse toContratacaoResponse(Seguro seguro) {
        if (seguro == null) {
            return null;
        }
        
        return ContratacaoSeguroResponse.builder()
                .id(seguro.getId())
                .cpf(seguro.getCpfCliente())
                .tipo(seguro.getTipo())
                .valorMensal(seguro.getValorMensal())
                .numeroApolice(seguro.getNumeroApolice())
                .dataContratacao(seguro.getDataContratacao())
                .mensagem(String.format("Seguro %s contratado com sucesso!", seguro.getTipo().name()))
                .build();
    }
    
    /**
     * Converte uma lista de Seguro para SimulacaoTodosSegurosResponse.
     *
     * @param cpf CPF do cliente
     * @param seguros Lista de seguros simulados
     * @return DTO de resposta com simulações para todos os tipos de seguros
     */
    public SimulacaoTodosSegurosResponse toSimulacaoTodosResponse(String cpf, List<Seguro> seguros) {
        if (seguros == null || seguros.isEmpty()) {
            return SimulacaoTodosSegurosResponse.builder()
                    .cpf(cpf)
                    .clienteEncontrado(false)
                    .simulacoes(List.of())
                    .build();
        }
        
        // Assume que todos os seguros são para o mesmo cliente
        boolean clienteEncontrado = seguros.stream()
                .findFirst()
                .map(Seguro::isClienteEncontrado)
                .orElse(false);
        
        List<SimulacaoSeguroResponse> simulacoes = seguros.stream()
                .map(this::toSimulacaoResponse)
                .toList();
                
        return SimulacaoTodosSegurosResponse.builder()
                .cpf(cpf)
                .clienteEncontrado(clienteEncontrado)
                .simulacoes(simulacoes)
                .build();
    }
    
    /**
     * Converte um Seguro para SimulacaoSeguroResponse.
     *
     * @param seguro Entidade de domínio Seguro
     * @return DTO de resposta de simulação de seguro
     */
    public SimulacaoSeguroResponse toSimulacaoResponse(Seguro seguro) {
        if (seguro == null) {
            return null;
        }
        
        return SimulacaoSeguroResponse.builder()
                .tipo(seguro.getTipo())
                .valorMensal(seguro.getValorMensal())
                .descricao(seguro.getTipo().getDescricao())
                .cobertura(seguro.getTipo().getCobertura())
                .clienteEncontrado(seguro.isClienteEncontrado())
                .build();
    }
}
