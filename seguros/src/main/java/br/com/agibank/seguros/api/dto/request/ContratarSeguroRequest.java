package br.com.agibank.seguros.api.dto.request;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Dados necessários para contratar um seguro")
public class ContratarSeguroRequest {
    
    @NotBlank(message = "CPF é obrigatório")
    @Schema(description = "CPF do cliente (apenas números)", example = "12345678901")
    private String cpf;
    
    @NotNull(message = "Tipo de seguro é obrigatório")
    @Schema(description = "Tipo de seguro a ser contratado", example = "BRONZE")
    private TipoSeguro tipo;
}
