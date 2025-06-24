package br.com.agibank.seguros.domain.repository;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.model.Seguro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeguroRepository {
    
    Seguro salvar(Seguro seguro);
    
    Optional<Seguro> buscarPorId(UUID id);
    
    Optional<Seguro> buscarPorNumeroApolice(String numeroApolice);
    
    List<Seguro> buscarPorCpfCliente(String cpfCliente);
    
    boolean existeSeguroAtivoPorCpfETipo(String cpfCliente, TipoSeguro tipo);
}
