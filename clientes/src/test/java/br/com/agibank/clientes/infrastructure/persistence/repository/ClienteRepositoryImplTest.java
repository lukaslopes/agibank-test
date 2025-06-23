package br.com.agibank.clientes.infrastructure.persistence.repository;

import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.fixture.ClienteFixture;
import br.com.agibank.clientes.infrastructure.persistence.entity.ClienteEntity;
import br.com.agibank.clientes.infrastructure.persistence.entity.EnderecoEmbeddable;
import br.com.agibank.clientes.infrastructure.persistence.mapper.ClienteEntityMapper;
import br.com.agibank.clientes.infrastructure.persistence.repository.jpa.ClienteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryImplTest {

    @Mock
    private ClienteJpaRepository jpaRepository;
    
    @Mock
    private ClienteEntityMapper clienteEntityMapper;

    @InjectMocks
    private ClienteRepositoryImpl clienteRepository;

    private Cliente cliente;
    private ClienteEntity clienteEntity;

    @BeforeEach
    void setUp() {
        cliente = ClienteFixture.umClienteSemId();
        clienteEntity = ClienteEntity.builder()
            .id(cliente.getId())
            .cpf(cliente.getCpf())
            .nome(cliente.getNome())
            .dataNascimento(cliente.getDataNascimento())
            .telefone(cliente.getTelefone())
            .endereco(EnderecoEmbeddable.builder()
                .logradouro(cliente.getEndereco().getLogradouro())
                .numero(cliente.getEndereco().getNumero())
                .complemento(cliente.getEndereco().getComplemento())
                .bairro(cliente.getEndereco().getBairro())
                .cidade(cliente.getEndereco().getCidade())
                .estado(cliente.getEndereco().getEstado())
                .cep(cliente.getEndereco().getCep())
                .build())
            .build();
    }

    @Test
    void listarTodos_DeveRetornarListaDeClientes() {
        when(jpaRepository.findAll()).thenReturn(List.of(clienteEntity));
        when(clienteEntityMapper.toDomain(clienteEntity)).thenReturn(cliente);

        var clientes = clienteRepository.listarTodos();

        assertNotNull(clientes);
        assertFalse(clientes.isEmpty());
        assertEquals(1, clientes.size());
        verify(jpaRepository).findAll();
        verify(clienteEntityMapper).toDomain(clienteEntity);
    }

    @Test
    void buscarPorId_DeveRetornarCliente_QuandoEncontrar() {
        when(jpaRepository.findById(clienteEntity.getId())).thenReturn(Optional.of(clienteEntity));
        when(clienteEntityMapper.toDomain(clienteEntity)).thenReturn(cliente);

        var clienteEncontrado = clienteRepository.buscarPorId(clienteEntity.getId());

        assertTrue(clienteEncontrado.isPresent());
        assertEquals(clienteEntity.getId(), clienteEncontrado.get().getId());
        verify(jpaRepository).findById(clienteEntity.getId());
        verify(clienteEntityMapper).toDomain(clienteEntity);
    }

    @Test
    void buscarPorId_DeveRetornarVazio_QuandoNaoEncontrar() {
        when(jpaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var clienteEncontrado = clienteRepository.buscarPorId(UUID.randomUUID());

        assertTrue(clienteEncontrado.isEmpty());
        verify(clienteEntityMapper, never()).toDomain(any(ClienteEntity.class));
    }

    @Test
    void buscarPorCpf_DeveRetornarCliente_QuandoEncontrar() {
        String cpf = "12345678901";
        when(jpaRepository.findByCpf(cpf)).thenReturn(Optional.of(clienteEntity));
        when(clienteEntityMapper.toDomain(clienteEntity)).thenReturn(cliente);
        
        var clienteEncontrado = clienteRepository.buscarPorCpf(cpf);
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(clienteEntity.getCpf(), clienteEncontrado.get().getCpf());
        verify(jpaRepository).findByCpf(cpf);
        verify(clienteEntityMapper).toDomain(clienteEntity);
    }

    @Test
    void salvar_DevePersistirCliente_QuandoNovo() {
        when(clienteEntityMapper.toEntity(cliente)).thenReturn(clienteEntity);
        when(jpaRepository.save(clienteEntity)).thenReturn(clienteEntity);
        when(clienteEntityMapper.toDomain(clienteEntity)).thenReturn(cliente);
        
        var clienteSalvo = clienteRepository.salvar(cliente);

        assertNotNull(clienteSalvo);
        assertEquals(cliente.getCpf(), clienteSalvo.getCpf());
        verify(clienteEntityMapper).toEntity(cliente);
        verify(jpaRepository).save(clienteEntity);
        verify(clienteEntityMapper).toDomain(clienteEntity);
    }

    @Test
    void deletar_DeveRemoverCliente_QuandoExistir() {
        doNothing().when(jpaRepository).deleteById(cliente.getId());

        clienteRepository.deletar(cliente);

        verify(jpaRepository).deleteById(cliente.getId());
    }

    @Test
    void existePorCpf_DeveRetornarTrue_QuandoCpfExistir() {
        when(jpaRepository.existsByCpf(anyString())).thenReturn(true);

        boolean existe = clienteRepository.existePorCpf("12345678901");

        assertTrue(existe);
        verify(jpaRepository).existsByCpf("12345678901");
    }
}
