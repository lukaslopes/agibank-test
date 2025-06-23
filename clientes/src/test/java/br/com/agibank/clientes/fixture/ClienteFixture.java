package br.com.agibank.clientes.fixture;

import br.com.agibank.clientes.api.dto.ClienteDTO;
import br.com.agibank.clientes.api.dto.ClienteDTOResponse;
import br.com.agibank.clientes.domain.model.Cliente;

import java.time.LocalDate;
import java.util.UUID;

public class ClienteFixture {
    
    public static final String CPF = "12345678901";
    public static final String NOME = "João da Silva";
    public static final String DATA_NASCIMENTO = "2000-01-01";
    public static final String TELEFONE = "11999999999";
    public static final String CLIENTE_ID = "123e4567-e89b-12d3-a456-426614174000";
    
    public static ClienteDTO umClienteDTO() {
        return ClienteDTO.builder()
                .cpf(CPF)
                .nome(NOME)
                .dataNascimento(LocalDate.parse(DATA_NASCIMENTO))
                .telefone(TELEFONE)
                .endereco(EnderecoFixture.umEnderecoDTO())
                .build();
    }
    
    public static ClienteDTOResponse umClienteDTOResponse() {
        return ClienteDTOResponse.builder()
                .id(UUID.fromString(CLIENTE_ID))
                .cpf(CPF)
                .nome(NOME)
                .dataNascimento(LocalDate.parse(DATA_NASCIMENTO))
                .telefone(TELEFONE)
                .endereco(EnderecoFixture.umEnderecoDTO())
                .build();
    }
    
    public static Cliente umCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.fromString(CLIENTE_ID));
        cliente.setCpf(CPF);
        cliente.setNome(NOME);
        cliente.setDataNascimento(LocalDate.parse(DATA_NASCIMENTO));
        cliente.setTelefone(TELEFONE);
        cliente.setEndereco(EnderecoFixture.umEndereco());
        return cliente;
    }
    
    public static Cliente umClienteSemId() {
        Cliente cliente = new Cliente();
        cliente.setCpf(CPF);
        cliente.setNome(NOME);
        cliente.setDataNascimento(LocalDate.parse(DATA_NASCIMENTO));
        cliente.setTelefone(TELEFONE);
        cliente.setEndereco(EnderecoFixture.umEndereco());
        return cliente;
    }
}
