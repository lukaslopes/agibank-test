package br.com.agibank.seguros.domain.model;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa um seguro no domínio da aplicação.
 * Esta é uma classe de domínio pura, sem dependências de frameworks ou infraestrutura.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Seguro {
    
    private UUID id;
    private String cpfCliente;
    private TipoSeguro tipo;
    private BigDecimal valorMensal;
    private String numeroApolice;
    private LocalDateTime dataContratacao;
    private boolean ativo;
    
    @Builder.Default
    private boolean clienteEncontrado = true;
    
    /**
     * Factory method para criar uma nova instância de Seguro com valores padrão.
     *
     * @param cpfCliente CPF do cliente
     * @param tipo Tipo do seguro
     * @param valorMensal Valor mensal do seguro
     * @param numeroApolice Número da apólice
     * @return Nova instância de Seguro
     */
    public static Seguro criarNovo(String cpfCliente, TipoSeguro tipo, BigDecimal valorMensal, String numeroApolice) {
        return Seguro.builder()
                .cpfCliente(cpfCliente)
                .tipo(tipo)
                .valorMensal(valorMensal)
                .numeroApolice(numeroApolice)
                .dataContratacao(LocalDateTime.now())
                .ativo(true)
                .clienteEncontrado(true)
                .build();
    }
    
    /**
     * Verifica se o seguro está ativo.
     * 
     * @return true se o seguro estiver ativo, false caso contrário
     */
    public boolean isAtivo() {
        return ativo;
    }
    
    /**
     * Verifica se o cliente foi encontrado.
     * 
     * @return true se o cliente foi encontrado, false caso contrário
     */
    public boolean isClienteEncontrado() {
        return clienteEncontrado;
    }
}
