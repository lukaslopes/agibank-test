package br.com.agibank.seguros.api.dto.response;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta da contratação de seguro")
public class ContratacaoSeguroResponse {
    
    @Schema(description = "ID da contratação do seguro")
    private UUID id;
    
    @Schema(description = "CPF do cliente", example = "12345678901")
    private String cpf;
    
    @Schema(description = "Tipo de seguro contratado", example = "BRONZE")
    private TipoSeguro tipo;
    
    @Schema(description = "Valor mensal do seguro contratado", example = "100.00")
    private BigDecimal valorMensal;
    
    @Schema(description = "Data e hora da contratação")
    private LocalDateTime dataContratacao;
    
    @Schema(description = "Número da apólice gerada")
    private String numeroApolice;
    
    @Schema(description = "Mensagem de confirmação da contratação")
    private String mensagem;
}
