package br.com.agibank.seguros.domain.usecase;

import java.util.List;

import br.com.agibank.seguros.domain.model.Seguro;

public interface SeguroUseCase {
    
    /**
     * Simula os valores para todos os tipos de seguros disponíveis para um cliente
     * 
     * @param cpf CPF do cliente
     * @return Lista de seguros simulados, um para cada tipo disponível
     */
    List<Seguro> simularTodosSeguros(String cpf);
    
    /**
     * Contrata um seguro para um cliente
     * 
     * @param seguro Dados do seguro a ser contratado
     * @return Seguro contratado com dados atualizados
     */
    Seguro contratarSeguro(Seguro seguro);
}
