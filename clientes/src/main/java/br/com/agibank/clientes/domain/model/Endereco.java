package br.com.agibank.clientes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um endereço no domínio da aplicação.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return java.util.Objects.equals(cep, endereco.cep) &&
               java.util.Objects.equals(logradouro, endereco.logradouro) &&
               java.util.Objects.equals(numero, endereco.numero) &&
               java.util.Objects.equals(complemento, endereco.complemento);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(cep, logradouro, numero, complemento);
    }
}
