package br.com.agibank.seguros.domain.usecase;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.exception.SeguroException;
import br.com.agibank.seguros.domain.model.Seguro;
import br.com.agibank.seguros.domain.repository.SeguroRepository;
import br.com.agibank.seguros.infrastructure.client.ClientesApiClient;
import br.com.agibank.seguros.fixture.SeguroFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeguroServiceImplTest {


    
    @Mock
    private SeguroRepository seguroRepository;
    
    @Mock
    private ClientesApiClient clientesApiClient;
    
    @InjectMocks
    private SeguroServiceImpl seguroService;
    
    @BeforeEach
    void setUp() {
        // Configuração de mocks será feita em cada teste conforme necessário
    }
    
    @Test
    void simularTodosSeguros_QuandoClienteExiste_DeveRetornarTresSimulacoesComValores() {
        when(clientesApiClient.clienteExiste(SeguroFixture.CPF_VALIDO)).thenReturn(true);
        
        var seguros = seguroService.simularTodosSeguros(SeguroFixture.CPF_VALIDO);
        
        assertNotNull(seguros);
        assertEquals(3, seguros.size());
        
        assertTrue(seguros.stream().anyMatch(s -> s.getTipo() == TipoSeguro.BRONZE));
        assertTrue(seguros.stream().anyMatch(s -> s.getTipo() == TipoSeguro.PRATA));
        assertTrue(seguros.stream().anyMatch(s -> s.getTipo() == TipoSeguro.OURO));
        
        seguros.forEach(seguro -> {
            assertTrue(seguro.getValorMensal().compareTo(BigDecimal.ZERO) > 0);
            assertTrue(seguro.isClienteEncontrado());
            assertEquals(SeguroFixture.CPF_VALIDO, seguro.getCpfCliente());
        });

        verify(clientesApiClient).clienteExiste(SeguroFixture.CPF_VALIDO);
    }

    @Test
    void simularTodosSeguros_QuandoClienteNaoExiste_DeveRetornarTresSimulacoesComValoresCalculados() {
        when(clientesApiClient.clienteExiste(SeguroFixture.CPF_VALIDO)).thenReturn(false);

        var seguros = seguroService.simularTodosSeguros(SeguroFixture.CPF_VALIDO);

        assertNotNull(seguros);
        assertEquals(3, seguros.size());


        assertTrue(seguros.stream().anyMatch(s -> s.getTipo() == TipoSeguro.BRONZE));
        assertTrue(seguros.stream().anyMatch(s -> s.getTipo() == TipoSeguro.PRATA));
        assertTrue(seguros.stream().anyMatch(s -> s.getTipo() == TipoSeguro.OURO));


        seguros.forEach(seguro -> {
            assertTrue(seguro.getValorMensal().compareTo(BigDecimal.ZERO) > 0,
                    "Valor mensal deve ser maior que zero para o tipo: " + seguro.getTipo());
            assertFalse(seguro.isClienteEncontrado());
            assertEquals(SeguroFixture.CPF_VALIDO, seguro.getCpfCliente());
        });

        verify(clientesApiClient).clienteExiste(SeguroFixture.CPF_VALIDO);
    }

    @Test
    void contratarSeguro_QuandoClienteExisteEDadosValidos_DeveRetornarSeguroCriado() {
        when(clientesApiClient.clienteExiste(SeguroFixture.CPF_VALIDO)).thenReturn(true);
        when(seguroRepository.existeSeguroAtivoPorCpfETipo(SeguroFixture.CPF_VALIDO, TipoSeguro.BRONZE)).thenReturn(false);

        Seguro seguroSalvo = SeguroFixture.umSeguroBronze().toBuilder()
                .id(UUID.randomUUID())
                .dataContratacao(java.time.LocalDateTime.now())
                .build();

        when(seguroRepository.salvar(any(Seguro.class))).thenReturn(seguroSalvo);

        Seguro resultado = seguroService.contratarSeguro(
                SeguroFixture.umSeguroBronze()
                        .toBuilder()
                        .id(null)
                        .dataContratacao(null)
                        .numeroApolice(null)
                        .valorMensal(null)
                        .ativo(false)
                        .build()
        );

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(SeguroFixture.CPF_VALIDO, resultado.getCpfCliente());
        assertEquals(TipoSeguro.BRONZE, resultado.getTipo());
        assertTrue(resultado.getValorMensal().compareTo(BigDecimal.ZERO) > 0);
        assertNotNull(resultado.getNumeroApolice());
        assertTrue(resultado.isAtivo());

        verify(seguroRepository).salvar(any(Seguro.class));
    }

    @Test
    void contratarSeguro_QuandoClienteNaoExiste_DeveLancarExcecao() {
        when(clientesApiClient.clienteExiste(SeguroFixture.CPF_VALIDO)).thenReturn(false);

        assertThrows(SeguroException.class, () ->
                seguroService.contratarSeguro(
                        Seguro.builder()
                                .cpfCliente(SeguroFixture.CPF_VALIDO)
                                .tipo(TipoSeguro.BRONZE)
                                .build()
                )
        );

        verify(seguroRepository, never()).salvar(any(Seguro.class));
    }

    @Test
    void contratarSeguro_QuandoSeguroJaExiste_DeveLancarExcecao() {
        when(clientesApiClient.clienteExiste(SeguroFixture.CPF_VALIDO)).thenReturn(true);
        when(seguroRepository.existeSeguroAtivoPorCpfETipo(SeguroFixture.CPF_VALIDO, TipoSeguro.BRONZE)).thenReturn(true);

        assertThrows(SeguroException.class, () ->
                seguroService.contratarSeguro(
                        Seguro.builder()
                                .cpfCliente(SeguroFixture.CPF_VALIDO)
                                .tipo(TipoSeguro.BRONZE)
                                .build()
                )
        );

        verify(seguroRepository, never()).salvar(any(Seguro.class));
    }
}
