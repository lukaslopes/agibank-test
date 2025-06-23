package br.com.agibank.clientes.fixture;

import br.com.agibank.clientes.api.dto.ClienteDTO;
import br.com.agibank.clientes.domain.model.Endereco;

public class EnderecoFixture {
    
    public static ClienteDTO.EnderecoDTO umEnderecoDTO() {
        return ClienteDTO.EnderecoDTO.builder()
                .logradouro("Rua Teste")
                .numero("123")
                .complemento("Casa 1")
                .bairro("Bairro Teste")
                .cidade("Cidade Teste")
                .estado("SP")
                .cep("12345678")
                .build();
    }
    
    public static Endereco umEndereco() {
        return Endereco.builder()
                .logradouro("Rua Teste")
                .numero("123")
                .complemento("Casa 1")
                .bairro("Bairro Teste")
                .cidade("Cidade Teste")
                .estado("SP")
                .cep("12345678")
                .build();
    }
}
