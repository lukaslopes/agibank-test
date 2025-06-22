package br.com.agibank.clientes.infrastructure.persistence.entity;

import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.model.Endereco;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(length = 20)
    private String telefone;

    @Embedded
    private EnderecoEmbeddable endereco;

    public Cliente toDomain() {
        return new Cliente(
            this.id,
            this.cpf,
            this.nome,
            this.dataNascimento,
            this.telefone,
            new Endereco(
                this.endereco.getLogradouro(),
                this.endereco.getNumero(),
                this.endereco.getComplemento(),
                this.endereco.getBairro(),
                this.endereco.getCidade(),
                this.endereco.getEstado(),
                this.endereco.getCep()
            )
        );
    }

    public static ClienteEntity fromDomain(Cliente cliente) {
        Endereco endereco = cliente.getEndereco();
        EnderecoEmbeddable enderecoEmbeddable = new EnderecoEmbeddable(
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
        
        return new ClienteEntity(
            cliente.getId(),
            cliente.getCpf(),
            cliente.getNome(),
            cliente.getDataNascimento(),
            cliente.getTelefone(),
            enderecoEmbeddable
        );
    }
}
