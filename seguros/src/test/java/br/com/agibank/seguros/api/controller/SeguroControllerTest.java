package br.com.agibank.seguros.api.controller;

import br.com.agibank.seguros.api.dto.request.ContratarSeguroRequest;
import br.com.agibank.seguros.api.dto.request.SimulacaoSeguroRequest;
import br.com.agibank.seguros.api.dto.response.ContratacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoTodosSegurosResponse;
import br.com.agibank.seguros.api.exceptionhandler.GlobalExceptionHandler;
import br.com.agibank.seguros.api.mapper.SeguroMapper;
import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.exception.SeguroException;
import br.com.agibank.seguros.domain.model.Seguro;
import br.com.agibank.seguros.domain.usecase.SeguroUseCase;
import br.com.agibank.seguros.fixture.SeguroFixture;
import br.com.agibank.seguros.infrastructure.client.ClientesApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class SeguroControllerTest {

    private static final String BASE_URL = "/seguros";
    
    @Mock
    private SeguroUseCase seguroUseCase;
    
    @Mock
    private SeguroMapper seguroMapper;
    
    @Mock
    private ClientesApiClient clientesApiClient;
    
    @Mock
    private MessageSource messageSource;
    
    @InjectMocks
    private SeguroController seguroController;
    
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeEach
    void setUp() {
        // Configure ObjectMapper to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        mockMvc = MockMvcBuilders.standaloneSetup(seguroController)
                .setControllerAdvice(new GlobalExceptionHandler(messageSource))
                .build();
    }
    
    @Test
    void simular_ClienteExiste_DeveRetornarSimulacaoCompleta() throws Exception {
        SimulacaoSeguroRequest request = SeguroFixture.umaSimulacaoSeguroRequest();
        List<Seguro> seguros = SeguroFixture.todosOsSeguros();
        SimulacaoTodosSegurosResponse response = SeguroFixture.umaSimulacaoTodosSegurosResponse();
        
        when(seguroUseCase.simularTodosSeguros(SeguroFixture.CPF_VALIDO))
                .thenReturn(seguros);
        when(seguroMapper.toSimulacaoTodosResponse(SeguroFixture.CPF_VALIDO, seguros))
                .thenReturn(response);
                
        mockMvc.perform(post(BASE_URL + "/simular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(SeguroFixture.CPF_VALIDO))
                .andExpect(jsonPath("$.clienteEncontrado").value(true))
                .andExpect(jsonPath("$.simulacoes", hasSize(3)))
                .andExpect(jsonPath("$.simulacoes[0].tipo").value(TipoSeguro.BRONZE.name()))
                .andExpect(jsonPath("$.simulacoes[1].tipo").value(TipoSeguro.PRATA.name()))
                .andExpect(jsonPath("$.simulacoes[2].tipo").value(TipoSeguro.OURO.name()));
        
        verify(seguroUseCase).simularTodosSeguros(SeguroFixture.CPF_VALIDO);
        verify(seguroMapper).toSimulacaoTodosResponse(SeguroFixture.CPF_VALIDO, seguros);
    }
    
    @Test
    void simular_ClienteNaoExiste_DeveRetornarClienteNaoEncontrado() throws Exception {
        SimulacaoSeguroRequest request = SeguroFixture.umaSimulacaoSeguroRequest();
        List<Seguro> seguros = SeguroFixture.todosOsSegurosClienteNaoEncontrado();
        SimulacaoTodosSegurosResponse response = new SimulacaoTodosSegurosResponse(
                SeguroFixture.CPF_VALIDO,
                false,
                List.of()
        );
        
        when(seguroUseCase.simularTodosSeguros(SeguroFixture.CPF_VALIDO))
                .thenReturn(seguros);
        when(seguroMapper.toSimulacaoTodosResponse(SeguroFixture.CPF_VALIDO, seguros))
                .thenReturn(response);
                
        mockMvc.perform(post(BASE_URL + "/simular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(SeguroFixture.CPF_VALIDO))
                .andExpect(jsonPath("$.clienteEncontrado").value(false));
        
        verify(seguroUseCase).simularTodosSeguros(SeguroFixture.CPF_VALIDO);
        verify(seguroMapper).toSimulacaoTodosResponse(SeguroFixture.CPF_VALIDO, seguros);
    }
    
    @Test
    void contratar_DadosValidos_DeveRetornarSeguroCriado() throws Exception {
        ContratarSeguroRequest request = SeguroFixture.umContratarSeguroRequest();
        Seguro seguro = SeguroFixture.umSeguroBronze();
        ContratacaoSeguroResponse response = SeguroFixture.umaContratacaoSeguroResponse();
        
        when(seguroMapper.toModel(any(ContratarSeguroRequest.class))).thenReturn(seguro);
        when(seguroUseCase.contratarSeguro(any(Seguro.class))).thenReturn(seguro);
        when(seguroMapper.toContratacaoResponse(seguro)).thenReturn(response);
        
        mockMvc.perform(post(BASE_URL + "/contratar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.cpf").value(SeguroFixture.CPF_VALIDO))
                .andExpect(jsonPath("$.tipo").value(TipoSeguro.BRONZE.name()))
                .andExpect(jsonPath("$.valorMensal").isNumber())
                .andExpect(jsonPath("$.numeroApolice").isNotEmpty());
        
        verify(seguroMapper).toModel(any(ContratarSeguroRequest.class));
        verify(seguroUseCase).contratarSeguro(any(Seguro.class));
        verify(seguroMapper).toContratacaoResponse(seguro);
    }
    
    @Test
    void contratar_ClienteNaoExiste_DeveRetornarBadRequest() throws Exception {
        ContratarSeguroRequest request = SeguroFixture.umContratarSeguroRequest();
        
        when(seguroMapper.toModel(any(ContratarSeguroRequest.class)))
                .thenReturn(SeguroFixture.umSeguroBronze());
        when(seguroUseCase.contratarSeguro(any(Seguro.class)))
                .thenThrow(new SeguroException("Cliente não encontrado"));
        
        mockMvc.perform(post(BASE_URL + "/contratar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        
        verify(seguroMapper).toModel(any(ContratarSeguroRequest.class));
        verify(seguroUseCase).contratarSeguro(any(Seguro.class));
    }
    
    @Test
    void contratar_SeguroJaExiste_DeveRetornarBadRequest() throws Exception {
        ContratarSeguroRequest request = SeguroFixture.umContratarSeguroRequest();
        
        when(seguroMapper.toModel(any(ContratarSeguroRequest.class)))
                .thenReturn(SeguroFixture.umSeguroBronze());
        when(seguroUseCase.contratarSeguro(any(Seguro.class)))
                .thenThrow(new SeguroException("Já existe um seguro ativo deste tipo para o cliente informado"));
        
        mockMvc.perform(post(BASE_URL + "/contratar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        
        verify(seguroMapper).toModel(any(ContratarSeguroRequest.class));
        verify(seguroUseCase).contratarSeguro(any(Seguro.class));
    }
}
