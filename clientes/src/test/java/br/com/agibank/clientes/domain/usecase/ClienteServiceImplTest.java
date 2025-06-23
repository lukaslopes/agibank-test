package br.com.agibank.clientes.domain.usecase;

import br.com.agibank.clientes.domain.exception.ClienteNaoEncontradoException;
import br.com.agibank.clientes.domain.exception.ClienteException;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.repository.ClienteRepository;
import br.com.agibank.clientes.fixture.ClienteFixture;
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
        cliente = ClienteFixture.umClienteSemId();
        clienteSalvo = ClienteFixture.umCliente();
        clienteSalvo.setId(UUID.randomUUID());
    }

    @Test
    void listarClientes_DeveRetornarListaDeClientes_QuandoExistirClientes() {
        when(clienteRepository.listarTodos()).thenReturn(List.of(clienteSalvo));
        List<Cliente> clientes = clienteService.listarClientes();
        assertNotNull(clientes);
        assertFalse(clientes.isEmpty());
        assertEquals(1, clientes.size());
        assertEquals(clienteSalvo.getId(), clientes.get(0).getId());
    }

    @Test
    void buscarClientePorId_DeveRetornarCliente_QuandoExistir() {
        when(clienteRepository.buscarPorId(clienteSalvo.getId()))
                .thenReturn(Optional.of(clienteSalvo));
        Cliente clienteEncontrado = clienteService.buscarClientePorId(clienteSalvo.getId());

        assertNotNull(clienteEncontrado);
        assertEquals(clienteSalvo.getId(), clienteEncontrado.getId());
    }

    @Test
    void buscarClientePorId_DeveLancarExcecao_QuandoNaoEncontrar() {

        UUID idInexistente = UUID.randomUUID();
        when(clienteRepository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, 
            () -> clienteService.buscarClientePorId(idInexistente));
    }

    @Test
    void cadastrarCliente_DeveSalvarCliente_QuandoDadosValidos() {

        when(clienteRepository.existePorCpf(cliente.getCpf())).thenReturn(false);
        when(clienteRepository.salvar(any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente clienteCadastrado = clienteService.cadastrarCliente(cliente);

        assertNotNull(clienteCadastrado);
        assertEquals(clienteSalvo.getId(), clienteCadastrado.getId());
        verify(clienteRepository, times(1)).salvar(cliente);
    }

    @Test
    void cadastrarCliente_DeveLancarExcecao_QuandoCpfJaExistir() {
        when(clienteRepository.existePorCpf(cliente.getCpf())).thenReturn(true);
        assertThrows(ClienteException.class, 
            () -> clienteService.cadastrarCliente(cliente));
        verify(clienteRepository, never()).salvar(any(Cliente.class));
    }

    @Test
    void atualizarCliente_DeveAtualizarCliente_QuandoDadosValidos() {
        cliente.setId(clienteSalvo.getId());
        
        when(clienteRepository.buscarPorId(clienteSalvo.getId()))
                .thenReturn(Optional.of(clienteSalvo));
        when(clienteRepository.salvar(any(Cliente.class))).thenReturn(clienteSalvo);

        Cliente clienteAtualizado = clienteService.atualizarCliente(cliente);

        assertNotNull(clienteAtualizado);
        assertEquals(clienteSalvo.getId(), clienteAtualizado.getId());
        verify(clienteRepository, times(1)).salvar(any(Cliente.class));
        verify(clienteRepository, times(1)).buscarPorId(clienteSalvo.getId());
    }

    @Test
    void removerCliente_DeveRemoverCliente_QuandoExistir() {
        when(clienteRepository.buscarPorId(clienteSalvo.getId()))
                .thenReturn(Optional.of(clienteSalvo));
        doNothing().when(clienteRepository).deletar(clienteSalvo);
        clienteService.removerCliente(clienteSalvo.getId());
        
        verify(clienteRepository, times(1)).deletar(clienteSalvo);
    }
}
