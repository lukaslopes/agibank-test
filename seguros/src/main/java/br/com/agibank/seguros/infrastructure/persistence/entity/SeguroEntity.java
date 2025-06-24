package br.com.agibank.seguros.infrastructure.persistence.entity;

import br.com.agibank.seguros.domain.enums.TipoSeguro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seguros")
public class SeguroEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    
    @Column(nullable = false, length = 11)
    private String cpfCliente;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoSeguro tipo;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMensal;
    
    @Column(nullable = false, unique = true, length = 50)
    private String numeroApolice;
    
    @Column(nullable = false)
    private LocalDateTime dataContratacao;
    
    @Column(nullable = false)
    private boolean ativo;
    
    @PrePersist
    protected void onCreate() {
        this.dataContratacao = LocalDateTime.now();
        this.ativo = true;
    }
}
