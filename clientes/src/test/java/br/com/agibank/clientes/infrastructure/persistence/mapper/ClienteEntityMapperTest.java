package br.com.agibank.clientes.infrastructure.persistence.mapper;

import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.model.Endereco;
import br.com.agibank.clientes.fixture.ClienteFixture;
import br.com.agibank.clientes.infrastructure.persistence.entity.ClienteEntity;
import br.com.agibank.clientes.infrastructure.persistence.entity.EnderecoEmbeddable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

class ClienteEntityMapperTest {

    private final ClienteEntityMapper mapper = new ClienteEntityMapper();

    @Test
    void toDomain_DeveMapearClienteEntityParaCliente() {
        ClienteEntity entity = ClienteEntity.builder()
            .id(UUID.fromString(ClienteFixture.CLIENTE_ID))
            .cpf(ClienteFixture.CPF)
            .nome(ClienteFixture.NOME)
            .dataNascimento(LocalDate.parse(ClienteFixture.DATA_NASCIMENTO))
            .telefone(ClienteFixture.TELEFONE)
            .endereco(EnderecoEmbeddable.builder()
                .logradouro("Rua Teste")
                .numero("123")
                .complemento("Apto 1")
                .bairro("Centro")
                .cidade("São Paulo")
                .estado("SP")
                .cep("01001000")
                .build())
            .build();

        Cliente result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getCpf(), result.getCpf());
        assertEquals(entity.getNome(), result.getNome());
        assertEquals(entity.getDataNascimento(), result.getDataNascimento());
        assertEquals(entity.getTelefone(), result.getTelefone());
        assertNotNull(result.getEndereco());
    }

    @Test
    void toDomain_DeveRetornarNull_QuandoEntradaForNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_DeveMapearClienteParaClienteEntity() {
        Cliente cliente = ClienteFixture.umCliente();

        ClienteEntity result = mapper.toEntity(cliente);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());
        assertEquals(cliente.getCpf(), result.getCpf());
        assertEquals(cliente.getNome(), result.getNome());
        assertEquals(cliente.getDataNascimento(), result.getDataNascimento());
        assertEquals(cliente.getTelefone(), result.getTelefone());
        assertNotNull(result.getEndereco());
    }

    @Test
    void toEntity_DeveRetornarNull_QuandoEntradaForNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEndereco_DeveMapearEnderecoEmbeddableParaEndereco() {
        EnderecoEmbeddable enderecoEmbeddable = EnderecoEmbeddable.builder()
            .logradouro("Rua Teste")
            .numero("123")
            .complemento("Apto 1")
            .bairro("Centro")
            .cidade("São Paulo")
            .estado("SP")
            .cep("01001000")
            .build();

        var result = mapper.toEndereco(enderecoEmbeddable);

        assertNotNull(result);
        assertEquals(enderecoEmbeddable.getLogradouro(), result.getLogradouro());
        assertEquals(enderecoEmbeddable.getNumero(), result.getNumero());
        assertEquals(enderecoEmbeddable.getComplemento(), result.getComplemento());
        assertEquals(enderecoEmbeddable.getBairro(), result.getBairro());
        assertEquals(enderecoEmbeddable.getCidade(), result.getCidade());
        assertEquals(enderecoEmbeddable.getEstado(), result.getEstado());
        assertEquals(enderecoEmbeddable.getCep(), result.getCep());
    }

    @Test
    void toEnderecoEmbeddable_DeveMapearEnderecoParaEnderecoEmbeddable() {
        var endereco = Endereco.builder()
            .logradouro("Rua Teste")
            .numero("123")
            .complemento("Apto 1")
            .bairro("Centro")
            .cidade("São Paulo")
            .estado("SP")
            .cep("01001000")
            .build();

        var result = mapper.toEnderecoEmbeddable(endereco);

        assertNotNull(result);
        assertEquals(endereco.getLogradouro(), result.getLogradouro());
        assertEquals(endereco.getNumero(), result.getNumero());
        assertEquals(endereco.getComplemento(), result.getComplemento());
        assertEquals(endereco.getBairro(), result.getBairro());
        assertEquals(endereco.getCidade(), result.getCidade());
        assertEquals(endereco.getEstado(), result.getEstado());
        assertEquals(endereco.getCep(), result.getCep());
    }
}
