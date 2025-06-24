package br.com.agibank.seguros.domain.usecase;

import br.com.agibank.seguros.api.dto.response.SimulacaoSeguroResponse;
import br.com.agibank.seguros.domain.enums.TipoSeguro;
import br.com.agibank.seguros.domain.exception.SeguroException;
import br.com.agibank.seguros.domain.model.Seguro;
import br.com.agibank.seguros.domain.repository.SeguroRepository;
import br.com.agibank.seguros.infrastructure.client.ClientesApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeguroServiceImpl implements SeguroUseCase {

    private static final String MSG_CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado com o CPF informado";
    private static final String MSG_SEGURO_JA_CONTRATADO = "Já existe um seguro ativo deste tipo para o cliente informado";
    
    private final SeguroRepository seguroRepository;
    private final ClientesApiClient clientesApiClient;
    
    @Override
    public List<Seguro> simularTodosSeguros(String cpf) {
        log.info("Iniciando simulação de seguros para o CPF: {}", cpf);
        
        boolean clienteExiste = false;
        try {
            log.debug("Verificando existência do cliente com CPF: {}", cpf);
            clienteExiste = clientesApiClient.clienteExiste(cpf);
            log.debug("Cliente com CPF {} {}", cpf, clienteExiste ? "encontrado" : "não encontrado");
        } catch (Exception e) {
            log.warn("Não foi possível verificar a existência do cliente com CPF: {}. Continuando com cliente inexistente. Erro: {}", 
                   cpf, e.getMessage());
            clienteExiste = false;
        }
        
        log.info("Criando simulações para o CPF: {}, cliente existe: {}", cpf, clienteExiste);
        
        List<Seguro> seguros = List.of(
            criarSeguroSimulacao(cpf, TipoSeguro.BRONZE, clienteExiste, calcularValorMensal(TipoSeguro.BRONZE)),
            criarSeguroSimulacao(cpf, TipoSeguro.PRATA, clienteExiste, calcularValorMensal(TipoSeguro.PRATA)),
            criarSeguroSimulacao(cpf, TipoSeguro.OURO, clienteExiste, calcularValorMensal(TipoSeguro.OURO))
        );
        
        log.info("Simulação concluída para o CPF: {}. Total de seguros simulados: {}", cpf, seguros.size());
        return seguros;
    }
    
    /**
     * Cria um objeto de seguro para simulação.
     * 
     * @param cpf CPF do cliente
     * @param tipo Tipo do seguro
     * @param clienteEncontrado Indica se o cliente foi encontrado
     * @param valorMensal Valor mensal calculado
     * @return Instância de Seguro para simulação
     */
    private Seguro criarSeguroSimulacao(String cpf, TipoSeguro tipo, boolean clienteEncontrado, BigDecimal valorMensal) {
        return Seguro.builder()
                .cpfCliente(cpf)
                .tipo(tipo)
                .valorMensal(valorMensal)
                .clienteEncontrado(clienteEncontrado)
                .build();
    }
    
    @Override
    @Transactional
    public Seguro contratarSeguro(Seguro seguro) {
        // Verifica se o cliente existe
        if (!clientesApiClient.clienteExiste(seguro.getCpfCliente())) {
            throw new SeguroException(MSG_CLIENTE_NAO_ENCONTRADO);
        }
        
        // Verifica se já existe um seguro ativo deste tipo para o cliente
        if (seguroRepository.existeSeguroAtivoPorCpfETipo(
                seguro.getCpfCliente(), 
                seguro.getTipo())) {
            throw new SeguroException(MSG_SEGURO_JA_CONTRATADO);
        }
        
        // Se o seguro ainda não tiver um número de apólice, gera um
        if (seguro.getNumeroApolice() == null) {
            seguro.setNumeroApolice(gerarNumeroApolice());
        }
        
        // Se o seguro ainda não tiver valor mensal, calcula
        if (seguro.getValorMensal() == null) {
            seguro.setValorMensal(calcularValorMensal(seguro.getTipo()));
        }
        
        // Marca como ativo e define a data de contratação
        seguro.setAtivo(true);
        seguro.setDataContratacao(java.time.LocalDateTime.now());
        
        // Salva o seguro
        Seguro seguroSalvo = seguroRepository.salvar(seguro);
        
        log.info("Seguro contratado com sucesso: {}", seguroSalvo);
        
        return seguroSalvo;
    }
    
    private BigDecimal calcularValorMensal(TipoSeguro tipo) {
        // Apenas para exemplo, poderia ter uma lógica mais complexa aqui
        return BigDecimal.valueOf(tipo.getValorBase())
                .multiply(BigDecimal.valueOf(tipo.getFatorMultiplicador()))
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    private String gerarNumeroApolice() {
        return "AP" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private SimulacaoSeguroResponse criarRespostaSimulacaoClienteNaoEncontrado(TipoSeguro tipo) {
        return SimulacaoSeguroResponse.builder()
                .tipo(tipo)
                .valorMensal(BigDecimal.ZERO)
                .descricao(tipo.getDescricao())
                .cobertura(tipo.getCobertura())
                .clienteEncontrado(false)
                .build();
    }
}
