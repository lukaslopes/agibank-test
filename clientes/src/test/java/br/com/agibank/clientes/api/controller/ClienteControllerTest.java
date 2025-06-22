package br.com.agibank.clientes.api.controller;

import br.com.agibank.clientes.api.mapper.ClienteMapper;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.usecase.ClienteUseCase;
import br.com.agibank.clientes.utils.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    private static final String API_URL = "/clientes";

    private MockMvc mockMvc;

    @Mock
    private ClienteUseCase clienteUseCase;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteController clienteController;

    private Cliente cliente;
    private Cliente clienteSalvo;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new com.agibank.clientes.api.exceptionhandler.GlobalExceptionHandler())
                .build();

        cliente = TestDataCreator.createCliente();
        clienteSalvo = TestDataCreator.createCliente();
        clienteSalvo.setId(UUID.randomUUID());
    }

    @Test
    void listar_DeveRetornarListaDeClientes_QuandoExistirClientes() throws Exception {
        // Arrange
        when(clienteUseCase.listarClientes()).thenReturn(List.of(clienteSalvo));
        when(clienteMapper.toCollectionModel(anyList())).thenReturn(TestDataCreator.createClienteModelList());

        // Act & Assert
        mockMvc.perform(get(API_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(clienteSalvo.getId().toString())));
    }

    @Test
    void buscar_DeveRetornarCliente_QuandoExistir() throws Exception {
        // Arrange
        when(clienteUseCase.buscarClientePorId(clienteSalvo.getId())).thenReturn(clienteSalvo);
        when(clienteMapper.toModel(any(Cliente.class))).thenReturn(TestDataCreator.createClienteModel(clienteSalvo));

        // Act & Assert
        mockMvc.perform(get(API_URL + "/{id}", clienteSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(clienteSalvo.getId().toString())))
                .andExpect(jsonPath("$.nome", is(clienteSalvo.getNome())));
    }

    @Test
    void adicionar_DeveRetornarClienteCriado_QuandoDadosValidos() throws Exception {
        // Arrange
        when(clienteMapper.toDomainObject(any())).thenReturn(cliente);
        when(clienteUseCase.cadastrarCliente(any(Cliente.class))).thenReturn(clienteSalvo);
        when(clienteMapper.toModel(any(Cliente.class))).thenReturn(TestDataCreator.createClienteModel(clienteSalvo));

        String jsonCliente = """
        {
            "cpf": "12345678901",
            "nome": "João da Silva",
            "dataNascimento": "1990-01-01",
            "telefone": "11999999999",
            "endereco": {
                "logradouro": "Rua das Flores",
                "numero": "123",
                "complemento": "Apto 101",
                "bairro": "Centro",
                "cidade": "São Paulo",
                "estado": "SP",
                "cep": "01001000"
            }
        }
        """;

        // Act & Assert
        mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCliente))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void atualizar_DeveRetornarClienteAtualizado_QuandoDadosValidos() throws Exception {
        // Arrange
        when(clienteMapper.toDomainObject(any())).thenReturn(cliente);
        when(clienteUseCase.atualizarCliente(eq(clienteSalvo.getId()), any(Cliente.class))).thenReturn(clienteSalvo);
        when(clienteMapper.toModel(any(Cliente.class))).thenReturn(TestDataCreator.createClienteModel(clienteSalvo));

        String jsonCliente = """
        {
            "cpf": "12345678901",
            "nome": "João da Silva Atualizado",
            "dataNascimento": "1990-01-01",
            "telefone": "11999999999",
            "endereco": {
                "logradouro": "Rua das Flores",
                "numero": "123",
                "complemento": "Apto 101",
                "bairro": "Centro",
                "cidade": "São Paulo",
                "estado": "SP",
                "cep": "01001000"
            }
        }
        """;

        // Act & Assert
        mockMvc.perform(put(API_URL + "/{id}", clienteSalvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCliente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(clienteSalvo.getNome())));
    }

    @Test
    void remover_DeveRetornarNoContent_QuandoClienteExistir() throws Exception {
        // Arrange
        doNothing().when(clienteUseCase).removerCliente(clienteSalvo.getId());

        // Act & Assert
        mockMvc.perform(delete(API_URL + "/{id}", clienteSalvo.getId()))
                .andExpect(status().isNoContent());

        verify(clienteUseCase, times(1)).removerCliente(clienteSalvo.getId());
    }
}
