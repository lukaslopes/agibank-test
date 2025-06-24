package br.com.agibank.seguros.infrastructure.persistence.repository;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.model.Seguro;
import br.com.agibank.seguros.domain.repository.SeguroRepository;
import br.com.agibank.seguros.infrastructure.persistence.entity.SeguroEntity;
import br.com.agibank.seguros.infrastructure.persistence.mapper.SeguroEntityMapper;
import br.com.agibank.seguros.infrastructure.persistence.repository.jpa.SeguroJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SeguroRepositoryImpl implements SeguroRepository {

    private final SeguroJpaRepository jpaRepository;
    private final SeguroEntityMapper mapper;

    @Override
    @Transactional
    public Seguro salvar(Seguro seguro) {
        SeguroEntity entity = mapper.toEntity(seguro);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seguro> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seguro> buscarPorNumeroApolice(String numeroApolice) {
        return jpaRepository.findByNumeroApolice(numeroApolice)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seguro> buscarPorCpfCliente(String cpfCliente) {
        return jpaRepository.findByCpfCliente(cpfCliente).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeSeguroAtivoPorCpfETipo(String cpfCliente, TipoSeguro tipo) {
        return jpaRepository.existsByCpfClienteAndTipoAndAtivoTrue(cpfCliente, tipo);
    }
}
