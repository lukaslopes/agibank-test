package br.com.agibank.seguros.api.mapper;

import br.com.agibank.seguros.api.dto.request.ContratarSeguroRequest;
import br.com.agibank.seguros.api.dto.response.ContratacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoTodosSegurosResponse;
import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.model.Seguro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.agibank.seguros.fixture.SeguroFixture.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SeguroMapperTest {

    @InjectMocks
    private final SeguroMapper mapper = new SeguroMapper();
    
    private final ContratarSeguroRequest request = umContratarSeguroRequest();
    private final Seguro seguro = umSeguroBronze();
    
    @Test
    void toModel_WithValidRequest_ShouldReturnSeguro() {
        // When
        Seguro result = mapper.toModel(request);
        
        // Then
        assertNotNull(result);
        assertEquals(CPF_VALIDO, result.getCpfCliente());
        assertEquals(TipoSeguro.BRONZE, result.getTipo());
        assertTrue(result.isClienteEncontrado());
    }
    
    @Test
    void toModel_WithNullRequest_ShouldReturnNull() {
        // When
        Seguro result = mapper.toModel(null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void toContratacaoResponse_WithValidSeguro_ShouldReturnResponse() {
        // When
        ContratacaoSeguroResponse response = mapper.toContratacaoResponse(seguro);
        
        // Then
        assertNotNull(response);
        assertEquals(seguro.getId(), response.getId());
        assertEquals(seguro.getCpfCliente(), response.getCpf());
        assertEquals(seguro.getTipo(), response.getTipo());
        assertEquals(seguro.getValorMensal(), response.getValorMensal());
        assertEquals(seguro.getNumeroApolice(), response.getNumeroApolice());
        assertEquals(seguro.getDataContratacao(), response.getDataContratacao());
        assertTrue(response.getMensagem().contains("contratado com sucesso"));
    }
    
    @Test
    void toContratacaoResponse_WithNullSeguro_ShouldReturnNull() {
        // When
        ContratacaoSeguroResponse response = mapper.toContratacaoResponse(null);
        
        // Then
        assertNull(response);
    }
    
    @Test
    void toSimulacaoResponse_WithValidSeguro_ShouldReturnResponse() {
        // When
        SimulacaoSeguroResponse response = mapper.toSimulacaoResponse(seguro);
        
        // Then
        assertNotNull(response);
        assertEquals(seguro.getTipo(), response.getTipo());
        assertEquals(seguro.getValorMensal(), response.getValorMensal());
        assertEquals(seguro.getTipo().getDescricao(), response.getDescricao());
        assertEquals(seguro.getTipo().getCobertura(), response.getCobertura());
        assertTrue(response.isClienteEncontrado());
    }
    
    @Test
    void toSimulacaoResponse_WithNullSeguro_ShouldReturnNull() {
        // When
        SimulacaoSeguroResponse response = mapper.toSimulacaoResponse(null);
        
        // Then
        assertNull(response);
    }
    
    @Test
    void toSimulacaoTodosResponse_WithValidInput_ShouldReturnResponse() {
        // Given
        List<Seguro> seguros = List.of(
            umSeguroBronze(),
            umSeguroPrata()
        );
        
        // When
        SimulacaoTodosSegurosResponse response = mapper.toSimulacaoTodosResponse(CPF_VALIDO, seguros);
        
        // Then
        assertNotNull(response);
        assertEquals(CPF_VALIDO, response.getCpf());
        assertTrue(response.isClienteEncontrado());
        assertEquals(2, response.getSimulacoes().size());
    }
    
    @Test
    void toSimulacaoTodosResponse_WithEmptyList_ShouldReturnEmptyResponse() {
        // When
        SimulacaoTodosSegurosResponse response = mapper.toSimulacaoTodosResponse(CPF_VALIDO, List.of());
        
        // Then
        assertNotNull(response);
        assertEquals(CPF_VALIDO, response.getCpf());
        assertFalse(response.isClienteEncontrado());
        assertTrue(response.getSimulacoes().isEmpty());
    }
    
    @Test
    void toSimulacaoTodosResponse_WithNullList_ShouldReturnEmptyResponse() {
        // When
        SimulacaoTodosSegurosResponse response = mapper.toSimulacaoTodosResponse(CPF_VALIDO, null);
        
        // Then
        assertNotNull(response);
        assertEquals(CPF_VALIDO, response.getCpf());
        assertFalse(response.isClienteEncontrado());
        assertTrue(response.getSimulacoes().isEmpty());
    }
}
