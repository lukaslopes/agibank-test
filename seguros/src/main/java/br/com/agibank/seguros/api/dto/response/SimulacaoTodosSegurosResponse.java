package br.com.agibank.seguros.api.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que contém a simulação para todos os tipos de seguros disponíveis.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta com a simulação para todos os tipos de seguros")
public class SimulacaoTodosSegurosResponse {
    
    @Schema(description = "CPF do cliente para o qual foi feita a simulação", example = "12345678901")
    private String cpf;
    
    @Schema(description = "Indica se o cliente foi encontrado no cadastro")
    private boolean clienteEncontrado;
    
    @ArraySchema(schema = @Schema(description = "Lista com as simulações para cada tipo de seguro"))
    private List<SimulacaoSeguroResponse> simulacoes;
    
    /**
     * Cria uma resposta de simulação para todos os tipos de seguros.
     * 
     * @param cpf CPF do cliente
     * @param clienteEncontrado indica se o cliente foi encontrado
     * @param simulacoes lista de simulações por tipo de seguro
     * @return instância de SimulacaoTodosSegurosResponse
     */
    public static SimulacaoTodosSegurosResponse of(String cpf, boolean clienteEncontrado, List<SimulacaoSeguroResponse> simulacoes) {
        return SimulacaoTodosSegurosResponse.builder()
                .cpf(cpf)
                .clienteEncontrado(clienteEncontrado)
                .simulacoes(simulacoes)
                .build();
    }
}
