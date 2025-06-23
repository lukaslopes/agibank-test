package br.com.agibank.clientes.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]*$", message = "Nome deve conter apenas letras e espaços")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser uma data passada")
    private LocalDate dataNascimento;
    
    private String telefone;
    
    @Valid
    @NotNull(message = "Endereço é obrigatório")
    private EnderecoDTO endereco;
    
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoDTO {
        @NotBlank(message = "CEP é obrigatório")
        private String cep;
        
        @NotBlank(message = "Logradouro é obrigatório")
        private String logradouro;
        
        @NotBlank(message = "Número é obrigatório")
        private String numero;
        
        private String complemento;
        
        @NotBlank(message = "Bairro é obrigatório")
        private String bairro;
        
        @NotBlank(message = "Cidade é obrigatória")
        private String cidade;
        
        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
        private String estado;
    }
}
    
    