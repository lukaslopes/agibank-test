package br.com.agibank.seguros.api.dto.response;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta da simulação de seguro")
public class SimulacaoSeguroResponse {
    
    @Schema(description = "Tipo de seguro simulado", example = "BRONZE")
    private TipoSeguro tipo;
    
    @Schema(description = "Valor mensal do seguro", example = "100.00")
    private BigDecimal valorMensal;
    
    @Schema(description = "Descrição do tipo de seguro", example = "Coberturas básicas")
    private String descricao;
    
    @Schema(description = "Coberturas incluídas no seguro")
    private String cobertura;
    
    @Schema(description = "Indica se o cliente foi encontrado no cadastro")
    private boolean clienteEncontrado;
}
