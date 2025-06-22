package br.com.agibank.clientes.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnderecoEmbeddable {

    @Column(name = "endereco_logradouro", nullable = false, length = 100)
    private String logradouro;

    @Column(name = "endereco_numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "endereco_complemento", length = 50)
    private String complemento;

    @Column(name = "endereco_bairro", nullable = false, length = 50)
    private String bairro;

    @Column(name = "endereco_cidade", nullable = false, length = 50)
    private String cidade;

    @Column(name = "endereco_estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "endereco_cep", nullable = false, length = 8)
    private String cep;
}
