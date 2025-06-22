package br.com.agibank.clientes.infrastructure.persistence.repository;

import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.infrastructure.persistence.entity.ClienteEntity;
import br.com.agibank.clientes.utils.TestDataCreator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    private EntityManager manager;

    @Mock
    private TypedQuery<ClienteEntity> query;

    @InjectMocks
    private ClienteRepositoryImpl clienteRepository;

    private Cliente cliente;
    private ClienteEntity clienteEntity;

    @BeforeEach
    void setUp() {
        cliente = TestDataCreator.createCliente();
        clienteEntity = ClienteEntity.fromDomain(cliente);
    }

    @Test
    void listarTodos_DeveRetornarListaDeClientes() {
        // Arrange
        when(manager.createQuery(anyString(), eq(ClienteEntity.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(clienteEntity));

        // Act
        var clientes = clienteRepository.listarTodos();

        // Assert
        assertNotNull(clientes);
        assertFalse(clientes.isEmpty());
        assertEquals(1, clientes.size());
        verify(manager).createQuery("from ClienteEntity", ClienteEntity.class);
    }

    @Test
    void buscarPorId_DeveRetornarCliente_QuandoEncontrar() {
        // Arrange
        when(manager.find(ClienteEntity.class, clienteEntity.getId())).thenReturn(clienteEntity);

        // Act
        var clienteEncontrado = clienteRepository.buscarPorId(clienteEntity.getId());

        // Assert
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(clienteEntity.getId(), clienteEncontrado.get().getId());
    }

    @Test
    void buscarPorId_DeveRetornarVazio_QuandoNaoEncontrar() {
        // Arrange
        when(manager.find(ClienteEntity.class, any(UUID.class))).thenReturn(null);

        // Act
        var clienteEncontrado = clienteRepository.buscarPorId(UUID.randomUUID());

        // Assert
        assertTrue(clienteEncontrado.isEmpty());
    }

    @Test
    void buscarPorCpf_DeveRetornarCliente_QuandoEncontrar() {
        // Arrange
        when(manager.createQuery(anyString(), eq(ClienteEntity.class))).thenReturn(query);
        when(query.setParameter(eq("cpf"), anyString())).thenReturn(query);
        when(query.getResultStream()).thenReturn(List.of(clienteEntity).stream());

        // Act
        var clienteEncontrado = clienteRepository.buscarPorCpf("12345678901");

        // Assert
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(clienteEntity.getCpf(), clienteEncontrado.get().getCpf());
    }

    @Test
    void salvar_DevePersistirCliente_QuandoNovo() {
        // Arrange
        when(manager.merge(any(ClienteEntity.class))).thenReturn(clienteEntity);

        // Act
        var clienteSalvo = clienteRepository.salvar(cliente);

        // Assert
        assertNotNull(clienteSalvo);
        assertEquals(cliente.getCpf(), clienteSalvo.getCpf());
    }

    @Test
    void deletar_DeveRemoverCliente_QuandoExistir() {
        // Arrange
        when(manager.find(ClienteEntity.class, cliente.getId())).thenReturn(clienteEntity);
        doNothing().when(manager).remove(clienteEntity);

        // Act
        clienteRepository.deletar(cliente);


        // Assert
        verify(manager).remove(clienteEntity);
    }


    @Test
    void existePorCpf_DeveRetornarTrue_QuandoCpfExistir() {
        // Arrange
        TypedQuery<Long> countQuery = mock(TypedQuery.class);
        when(manager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(eq("cpf"), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // Act
        boolean existe = clienteRepository.existePorCpf("12345678901");

        // Assert
        assertTrue(existe);
    }
}
