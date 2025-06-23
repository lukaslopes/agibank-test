package br.com.agibank.clientes.infrastructure.persistence.repository.jpa;

import br.com.agibank.clientes.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, UUID> {
    Optional<ClienteEntity> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
