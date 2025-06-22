package br.com.agibank.clientes.domain.usecase;

import br.com.agibank.clientes.domain.exception.ClienteNaoEncontradoException;
import br.com.agibank.clientes.domain.exception.NegocioException;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.repository.ClienteRepository;
import br.com.agibank.clientes.utils.TestDataCreator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private Cliente clienteSalvo;

    @BeforeEach
    void setUp() {
        cliente = TestDataCreator.createCliente();
        clienteSalvo = TestDataCreator.createCliente();
        clienteSalvo.setId(UUID.randomUUID());
    }

    @Test
    void listarClientes_DeveRetornarListaDeClientes_QuandoExistirClientes() {
        // Arrange
        when(clienteRepository.listarTodos()).thenReturn(List.of(clienteSalvo));

        // Act
        List<Cliente> clientes = clienteService.listarClientes();

        // Assert
        assertNotNull(clientes);
        assertFalse(clientes.isEmpty());
        assertEquals(1, clientes.size());
        assertEquals(clienteSalvo.getId(), clientes.get(0).getId());
    }

    @Test
    void buscarClientePorId_DeveRetornarCliente_QuandoExistir() {
        // Arrange
        when(clienteRepository.buscarPorId(clienteSalvo.getId()))
                .thenReturn(Optional.of(clienteSalvo));

        // Act
        Cliente clienteEncontrado = clienteService.buscarClientePorId(clienteSalvo.getId());

        // Assert
        assertNotNull(clienteEncontrado);
        assertEquals(clienteSalvo.getId(), clienteEncontrado.getId());
    }

    @Test
    void buscarClientePorId_DeveLancarExcecao_QuandoNaoEncontrar() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(clienteRepository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNaoEncontradoException.class, 
            () -> clienteService.buscarClientePorId(idInexistente));
    }

    @Test
    void cadastrarCliente_DeveSalvarCliente_QuandoDadosValidos() {
        // Arrange
        when(clienteRepository.existePorCpf(cliente.getCpf())).thenReturn(false);
        when(clienteRepository.salvar(any(Cliente.class))).thenReturn(clienteSalvo);

        // Act
        Cliente clienteCadastrado = clienteService.cadastrarCliente(cliente);

        // Assert
        assertNotNull(clienteCadastrado);
        assertEquals(clienteSalvo.getId(), clienteCadastrado.getId());
        verify(clienteRepository, times(1)).salvar(cliente);
    }

    @Test
    void cadastrarCliente_DeveLancarExcecao_QuandoCpfJaExistir() {
        // Arrange
        when(clienteRepository.existePorCpf(cliente.getCpf())).thenReturn(true);

        // Act & Assert
        assertThrows(NegocioException.class, 
            () -> clienteService.cadastrarCliente(cliente));
        verify(clienteRepository, never()).salvar(any(Cliente.class));
    }

    @Test
    void atualizarCliente_DeveAtualizarCliente_QuandoDadosValidos() {
        // Arrange
        when(clienteRepository.buscarPorId(clienteSalvo.getId()))
                .thenReturn(Optional.of(clienteSalvo));
        when(clienteRepository.existePorCpf(cliente.getCpf())).thenReturn(false);
        when(clienteRepository.salvar(any(Cliente.class))).thenReturn(clienteSalvo);

        // Act
        Cliente clienteAtualizado = clienteService.atualizarCliente(clienteSalvo.getId(), cliente);

        // Assert
        assertNotNull(clienteAtualizado);
        assertEquals(clienteSalvo.getId(), clienteAtualizado.getId());
        verify(clienteRepository, times(1)).salvar(any(Cliente.class));
    }

    @Test
    void removerCliente_DeveRemoverCliente_QuandoExistir() {
        // Arrange
        when(clienteRepository.buscarPorId(clienteSalvo.getId()))
                .thenReturn(Optional.of(clienteSalvo));
        doNothing().when(clienteRepository).deletar(clienteSalvo);

        // Act
        clienteService.removerCliente(clienteSalvo.getId());

        // Assert
        verify(clienteRepository, times(1)).deletar(clienteSalvo);
    }
}
