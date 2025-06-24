package br.com.agibank.seguros.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para requisição de simulação de seguro.
 * Retorna os valores para todos os tipos de seguros disponíveis.
 */
@Data
@Schema(description = "Dados necessários para simular os valores dos seguros")
public class SimulacaoSeguroRequest {
    
    @NotBlank(message = "CPF é obrigatório")
    @Schema(description = "CPF do cliente (apenas números)", example = "12345678901")
    private String cpf;
}
