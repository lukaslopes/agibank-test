package br.com.agibank.seguros.infrastructure.persistence.mapper;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.model.Seguro;
import br.com.agibank.seguros.infrastructure.persistence.entity.SeguroEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.agibank.seguros.fixture.SeguroFixture.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SeguroEntityMapperTest {

    @InjectMocks
    private final SeguroEntityMapper mapper = new SeguroEntityMapper();
    
    private final Seguro domain = umSeguroBronze();
    private final SeguroEntity entity = SeguroEntity.builder()
            .id(domain.getId())
            .cpfCliente(domain.getCpfCliente())
            .tipo(domain.getTipo())
            .valorMensal(domain.getValorMensal())
            .numeroApolice(domain.getNumeroApolice())
            .dataContratacao(domain.getDataContratacao())
            .ativo(domain.isAtivo())
            .build();
    
    @Test
    void toDomain_WithValidEntity_ShouldReturnDomain() {
        // When
        Seguro result = mapper.toDomain(entity);
        
        // Then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getCpfCliente(), result.getCpfCliente());
        assertEquals(entity.getTipo(), result.getTipo());
        assertEquals(entity.getValorMensal(), result.getValorMensal());
        assertEquals(entity.getNumeroApolice(), result.getNumeroApolice());
        assertEquals(entity.getDataContratacao(), result.getDataContratacao());
        assertEquals(entity.isAtivo(), result.isAtivo());
        assertTrue(result.isClienteEncontrado());
    }
    
    @Test
    void toDomain_WithNullEntity_ShouldReturnNull() {
        // When
        Seguro result = mapper.toDomain(null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void toEntity_WithValidDomain_ShouldReturnEntity() {
        // When
        SeguroEntity result = mapper.toEntity(domain);
        
        // Then
        assertNotNull(result);
        assertEquals(domain.getId(), result.getId());
        assertEquals(domain.getCpfCliente(), result.getCpfCliente());
        assertEquals(domain.getTipo(), result.getTipo());
        assertEquals(domain.getValorMensal(), result.getValorMensal());
        assertEquals(domain.getNumeroApolice(), result.getNumeroApolice());
        assertEquals(domain.getDataContratacao(), result.getDataContratacao());
        assertEquals(domain.isAtivo(), result.isAtivo());
    }
    
    @Test
    void toEntity_WithNullDomain_ShouldReturnNull() {
        // When
        SeguroEntity result = mapper.toEntity(null);
        
        // Then
        assertNull(result);
    }
    
    @Test
    void toEntity_WithDomainHavingNullValues_ShouldHandleGracefully() {
        // Given
        Seguro partialDomain = Seguro.builder()
                .id(domain.getId())
                .cpfCliente(CPF_VALIDO)
                .tipo(TipoSeguro.BRONZE)
                .build();
        
        // When
        SeguroEntity result = mapper.toEntity(partialDomain);
        
        // Then
        assertNotNull(result);
        assertEquals(partialDomain.getId(), result.getId());
        assertEquals(partialDomain.getCpfCliente(), result.getCpfCliente());
        assertEquals(partialDomain.getTipo(), result.getTipo());
        assertNull(result.getValorMensal());
        assertNull(result.getNumeroApolice());
        assertNull(result.getDataContratacao());
        assertFalse(result.isAtivo());
    }
}
