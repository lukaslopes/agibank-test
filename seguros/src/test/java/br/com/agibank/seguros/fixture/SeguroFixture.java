package br.com.agibank.seguros.fixture;

import br.com.agibank.seguros.api.dto.request.ContratarSeguroRequest;
import br.com.agibank.seguros.api.dto.request.SimulacaoSeguroRequest;
import br.com.agibank.seguros.api.dto.response.ContratacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoTodosSegurosResponse;
import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.model.Seguro;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SeguroFixture {
    
    public static final String CPF_VALIDO = "12345678909";
    public static final String CPF_INVALIDO = "123";
    public static final String NUMERO_APOLICE = "APL123456";
    public static final String ID_SEGURO = "550e8400-e29b-41d4-a716-446655440000";
    
    public static Seguro umSeguroBronze() {
        return Seguro.builder()
                .id(UUID.fromString(ID_SEGURO))
                .cpfCliente(CPF_VALIDO)
                .tipo(TipoSeguro.BRONZE)
                .valorMensal(new BigDecimal("50.00"))
                .numeroApolice(NUMERO_APOLICE)
                .dataContratacao(LocalDateTime.now())
                .ativo(true)
                .clienteEncontrado(true)
                .build();
    }
    
    public static Seguro umSeguroPrata() {
        return umSeguroBronze().toBuilder()
                .tipo(TipoSeguro.PRATA)
                .valorMensal(new BigDecimal("100.00"))
                .build();
    }
    
    public static Seguro umSeguroOuro() {
        return umSeguroBronze().toBuilder()
                .tipo(TipoSeguro.OURO)
                .valorMensal(new BigDecimal("200.00"))
                .build();
    }
    
    public static List<Seguro> todosOsSeguros() {
        return List.of(
                umSeguroBronze(),
                umSeguroPrata(),
                umSeguroOuro()
        );
    }
    
    public static List<Seguro> todosOsSegurosClienteNaoEncontrado() {
        return List.of(
                umSeguroBronze().toBuilder().clienteEncontrado(false).valorMensal(BigDecimal.ZERO).build(),
                umSeguroPrata().toBuilder().clienteEncontrado(false).valorMensal(BigDecimal.ZERO).build(),
                umSeguroOuro().toBuilder().clienteEncontrado(false).valorMensal(BigDecimal.ZERO).build()
        );
    }
    
    public static ContratarSeguroRequest umContratarSeguroRequest() {
        return ContratarSeguroRequest.builder()
                .cpf(CPF_VALIDO)
                .tipo(TipoSeguro.BRONZE)
                .build();
    }
    
    public static SimulacaoSeguroRequest umaSimulacaoSeguroRequest() {
        SimulacaoSeguroRequest request = new SimulacaoSeguroRequest();
        request.setCpf(CPF_VALIDO);
        return request;
    }
    
    public static ContratacaoSeguroResponse umaContratacaoSeguroResponse() {
        return ContratacaoSeguroResponse.builder()
                .id(UUID.fromString(ID_SEGURO))
                .cpf(CPF_VALIDO)
                .tipo(TipoSeguro.BRONZE)
                .valorMensal(new BigDecimal("50.00"))
                .numeroApolice(NUMERO_APOLICE)
                .dataContratacao(LocalDateTime.now())
                .build();
    }
    
    public static SimulacaoSeguroResponse umaSimulacaoSeguroResponse(TipoSeguro tipo) {
        return SimulacaoSeguroResponse.builder()
                .tipo(tipo)
                .descricao(tipo.getDescricao())
                .cobertura(tipo.getCobertura())
                .valorMensal(new BigDecimal(tipo.getValorBase() * 1.5))
                .clienteEncontrado(true)
                .build();
    }
    
    public static SimulacaoTodosSegurosResponse umaSimulacaoTodosSegurosResponse() {
        return SimulacaoTodosSegurosResponse.builder()
                .cpf(CPF_VALIDO)
                .clienteEncontrado(true)
                .simulacoes(List.of(
                        umaSimulacaoSeguroResponse(TipoSeguro.BRONZE),
                        umaSimulacaoSeguroResponse(TipoSeguro.PRATA),
                        umaSimulacaoSeguroResponse(TipoSeguro.OURO)
                ))
                .build();
    }
}
