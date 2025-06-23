package br.com.agibank.clientes.api.controller;

import br.com.agibank.clientes.api.dto.ClienteDTO;
import br.com.agibank.clientes.api.dto.ClienteDTOResponse;
import br.com.agibank.clientes.api.exceptionhandler.GlobalExceptionHandler;
import br.com.agibank.clientes.api.mapper.ClienteMapper;
import br.com.agibank.clientes.domain.exception.ClienteNaoEncontradoException;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.usecase.ClienteUseCase;
import br.com.agibank.clientes.fixture.ClienteFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    private static final String API_URL = "/clientes";
    
    @Mock
    private ClienteUseCase clienteUseCase;
    
    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente;
    private Cliente clienteSalvo;
    private ClienteDTOResponse clienteDTOResponse;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Configure ObjectMapper to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .build();

        cliente = ClienteFixture.umClienteSemId();
        clienteSalvo = ClienteFixture.umCliente();
        clienteDTOResponse = ClienteFixture.umClienteDTOResponse();
    }

    @Test
    void listar_DeveRetornarListaVazia_QuandoNaoExistirClientes() throws Exception {
        when(clienteUseCase.listarClientes()).thenReturn(Collections.emptyList());
        when(clienteMapper.toDTOList(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get(API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(clienteUseCase).listarClientes();
        verify(clienteMapper).toDTOList(Collections.emptyList());
    }

    @Test
    void listar_DeveRetornarListaDeClientes_QuandoExistirClientes() throws Exception {
        List<Cliente> clientes = List.of(clienteSalvo);
        when(clienteUseCase.listarClientes()).thenReturn(clientes);
        when(clienteMapper.toDTOList(clientes)).thenReturn(List.of(clienteDTOResponse));
        mockMvc.perform(get(API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ClienteFixture.CLIENTE_ID)))
                .andExpect(jsonPath("$[0].nome", is(ClienteFixture.NOME)))
                .andExpect(jsonPath("$[0].cpf", is(ClienteFixture.CPF)));

        verify(clienteUseCase).listarClientes();
        verify(clienteMapper).toDTOList(clientes);
    }

    @Test
    void buscar_DeveRetornarCliente_QuandoExistir() throws Exception {
        when(clienteUseCase.buscarClientePorId(UUID.fromString(ClienteFixture.CLIENTE_ID))).thenReturn(clienteSalvo);
        when(clienteMapper.toDTO(clienteSalvo)).thenReturn(clienteDTOResponse);
        mockMvc.perform(get(API_URL + "/{id}", ClienteFixture.CLIENTE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ClienteFixture.CLIENTE_ID)))
                .andExpect(jsonPath("$.nome", is(ClienteFixture.NOME)))
                .andExpect(jsonPath("$.cpf", is(ClienteFixture.CPF)));

        verify(clienteUseCase).buscarClientePorId(UUID.fromString(ClienteFixture.CLIENTE_ID));
        verify(clienteMapper).toDTO(clienteSalvo);
    }

    @Test
    void buscar_DeveRetornarNotFound_QuandoClienteNaoExistir() throws Exception {
        UUID idInexistente = UUID.randomUUID();
        when(clienteUseCase.buscarClientePorId(idInexistente))
                .thenThrow(new ClienteNaoEncontradoException(idInexistente));
        mockMvc.perform(get(API_URL + "/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", containsString("Not Found")));

        verify(clienteUseCase).buscarClientePorId(idInexistente);
    }

    @Test
    void adicionar_DeveRetornarClienteCriado_QuandoDadosValidos() throws Exception {
        ClienteDTO novoClienteDTO = ClienteFixture.umClienteDTO();
        when(clienteMapper.toDomain(any(ClienteDTO.class))).thenReturn(cliente);
        when(clienteUseCase.cadastrarCliente(any(Cliente.class))).thenReturn(clienteSalvo);
        when(clienteMapper.toDTO(clienteSalvo)).thenReturn(clienteDTOResponse);
        mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoClienteDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value(ClienteFixture.NOME));

        verify(clienteMapper).toDomain(any(ClienteDTO.class));
        verify(clienteUseCase).cadastrarCliente(any(Cliente.class));
        verify(clienteMapper).toDTO(clienteSalvo);
    }

    @Test
    void adicionar_DeveRetornarBadRequest_QuandoDadosInvalidos() throws Exception {
        // Create an invalid DTO using the builder with null required fields
        ClienteDTO clienteInvalido = ClienteDTO.builder().build();
        
        mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_DeveRetornarClienteAtualizado_QuandoDadosValidos() throws Exception {
        String novoNome = "Novo Nome";
        Cliente clienteAtualizado = ClienteFixture.umCliente();
        clienteAtualizado.setNome(novoNome);

        ClienteDTOResponse clienteAtualizadoResponse = ClienteFixture.umClienteDTOResponse();
        clienteAtualizadoResponse.setNome(novoNome);

        when(clienteUseCase.buscarClientePorId(UUID.fromString(ClienteFixture.CLIENTE_ID))).thenReturn(clienteSalvo);
        when(clienteUseCase.atualizarCliente(any(Cliente.class))).thenReturn(clienteAtualizado);
        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteAtualizadoResponse);

        ClienteDTO clienteAtualizacao = ClienteFixture.umClienteDTO();
        clienteAtualizacao.setNome(novoNome);
        
        mockMvc.perform(put(API_URL + "/{id}", ClienteFixture.CLIENTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteAtualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(novoNome));

        verify(clienteUseCase).buscarClientePorId(UUID.fromString(ClienteFixture.CLIENTE_ID));
        verify(clienteMapper).updateFromDTO(any(Cliente.class), any(ClienteDTO.class));
        verify(clienteUseCase).atualizarCliente(any(Cliente.class));
        verify(clienteMapper).toDTO(any(Cliente.class));
    }

    @Test
    void atualizar_DeveRetornarNotFound_QuandoClienteNaoExistir() throws Exception {
        UUID idInexistente = UUID.randomUUID();
        when(clienteUseCase.buscarClientePorId(idInexistente))
                .thenThrow(new ClienteNaoEncontradoException(idInexistente));
        mockMvc.perform(put(API_URL + "/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ClienteFixture.umClienteDTO())))
                .andExpect(status().isNotFound());

        verify(clienteUseCase).buscarClientePorId(idInexistente);
        verify(clienteUseCase, never()).atualizarCliente(any(Cliente.class));
    }

    @Test
    void remover_DeveRetornarNoContent_QuandoClienteExistir() throws Exception {
        UUID clienteId = UUID.fromString(ClienteFixture.CLIENTE_ID);
        doNothing().when(clienteUseCase).removerCliente(clienteId);
        mockMvc.perform(delete(API_URL + "/{id}", ClienteFixture.CLIENTE_ID))
                .andExpect(status().isNoContent());

        verify(clienteUseCase).removerCliente(clienteId);
    }

    @Test
    void remover_DeveRetornarNoContent_QuandoClienteNaoExistir() throws Exception {
        UUID idInexistente = UUID.randomUUID();
        doThrow(new ClienteNaoEncontradoException(idInexistente))
                .when(clienteUseCase).removerCliente(idInexistente);
        mockMvc.perform(delete(API_URL + "/{id}", idInexistente))
                .andExpect(status().isNotFound());

        verify(clienteUseCase).removerCliente(idInexistente);
    }
}
