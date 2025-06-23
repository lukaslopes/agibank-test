package br.com.agibank.clientes.api.mapper;

import br.com.agibank.clientes.api.dto.ClienteDTO;
import br.com.agibank.clientes.api.dto.ClienteDTOResponse;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.fixture.ClienteFixture;
import br.com.agibank.clientes.fixture.EnderecoFixture;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteMapperTest {

    private final ClienteMapper mapper = new ClienteMapper();

    @Test
    void toDomain_DeveMapearClienteDtoParaCliente() {
        ClienteDTO dto = ClienteFixture.umClienteDTO();

        Cliente result = mapper.toDomain(dto);

        assertNotNull(result);
        assertEquals(dto.getCpf(), result.getCpf());
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getDataNascimento(), result.getDataNascimento());
        assertEquals(dto.getTelefone(), result.getTelefone());
        assertNotNull(result.getEndereco());
    }

    @Test
    void toDomain_DeveRetornarNull_QuandoEntradaForNull() {
        assertNull(mapper.toDomain((ClienteDTO) null));
    }

    @Test
    void toDTO_DeveMapearClienteParaClienteDTOResponse() {
        Cliente cliente = ClienteFixture.umCliente();

        ClienteDTOResponse result = mapper.toDTO(cliente);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());
        assertEquals(cliente.getCpf(), result.getCpf());
        assertEquals(cliente.getNome(), result.getNome());
        assertEquals(cliente.getDataNascimento(), result.getDataNascimento());
        assertEquals(cliente.getTelefone(), result.getTelefone());
        assertNotNull(result.getEndereco());
    }

    @Test
    void toDTO_DeveRetornarNull_QuandoEntradaForNull() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void toDTOList_DeveMapearListaDeClientesParaListaDeDTOResponses() {
        List<Cliente> clientes = List.of(ClienteFixture.umCliente());

        var result = mapper.toDTOList(clientes);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(clientes.get(0).getId(), result.get(0).getId());
    }

    @Test
    void toDTOList_DeveRetornarNull_QuandoEntradaForNull() {
        assertNull(mapper.toDTOList(null));
    }

    @Test
    void toEnderecoDomain_DeveMapearEnderecoDtoParaEndereco() {
        var enderecoDTO = EnderecoFixture.umEnderecoDTO();

        var result = mapper.toEnderecoDomain(enderecoDTO);

        assertNotNull(result);
        assertEquals(enderecoDTO.getLogradouro(), result.getLogradouro());
        assertEquals(enderecoDTO.getNumero(), result.getNumero());
        assertEquals(enderecoDTO.getComplemento(), result.getComplemento());
        assertEquals(enderecoDTO.getBairro(), result.getBairro());
        assertEquals(enderecoDTO.getCidade(), result.getCidade());
        assertEquals(enderecoDTO.getEstado(), result.getEstado());
        assertEquals(enderecoDTO.getCep(), result.getCep());
    }

    @Test
    void toEnderecoDTO_DeveMapearEnderecoParaEnderecoDto() {
        var endereco = EnderecoFixture.umEndereco();

        var result = mapper.toEnderecoDTO(endereco);

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
