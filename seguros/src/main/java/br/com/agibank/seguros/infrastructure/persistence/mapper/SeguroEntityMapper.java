package br.com.agibank.seguros.infrastructure.persistence.mapper;

import br.com.agibank.seguros.domain.model.Seguro;
import br.com.agibank.seguros.infrastructure.persistence.entity.SeguroEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável por converter entre a entidade de persistência e o domínio de Seguro.
 */
@Component
public class SeguroEntityMapper {

    /**
     * Converte uma entidade de persistência para o domínio.
     *
     * @param entity Entidade de persistência
     * @return Instância do domínio Seguro
     */
    public Seguro toDomain(SeguroEntity entity) {
        if (entity == null) {
            return null;
        }

        return Seguro.builder()
                .id(entity.getId())
                .cpfCliente(entity.getCpfCliente())
                .tipo(entity.getTipo())
                .valorMensal(entity.getValorMensal())
                .numeroApolice(entity.getNumeroApolice())
                .dataContratacao(entity.getDataContratacao())
                .ativo(entity.isAtivo())
                .clienteEncontrado(true) // Assume que se existe na base, o cliente foi encontrado
                .build();
    }

    /**
     * Converte um domínio para entidade de persistência.
     *
     * @param domain Instância do domínio Seguro
     * @return Entidade de persistência
     */
    public SeguroEntity toEntity(Seguro domain) {
        if (domain == null) {
            return null;
        }

        return SeguroEntity.builder()
                .id(domain.getId())
                .cpfCliente(domain.getCpfCliente())
                .tipo(domain.getTipo())
                .valorMensal(domain.getValorMensal())
                .numeroApolice(domain.getNumeroApolice())
                .dataContratacao(domain.getDataContratacao())
                .ativo(domain.isAtivo())
                .build();
    }
}
