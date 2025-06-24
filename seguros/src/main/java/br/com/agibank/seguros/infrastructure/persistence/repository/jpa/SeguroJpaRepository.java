package br.com.agibank.seguros.infrastructure.persistence.repository.jpa;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.infrastructure.persistence.entity.SeguroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeguroJpaRepository extends JpaRepository<SeguroEntity, UUID> {
    
    Optional<SeguroEntity> findByNumeroApolice(String numeroApolice);
    
    List<SeguroEntity> findByCpfCliente(String cpfCliente);
    
    boolean existsByCpfClienteAndTipoAndAtivoTrue(String cpfCliente, TipoSeguro tipo);
}
