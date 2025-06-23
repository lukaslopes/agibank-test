package br.com.agibank.clientes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private UUID id;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private Endereco endereco;
}
