package br.com.simplesdental.entity.profissional;

import br.com.simplesdental.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@ToString
@Table(name = "profissionais")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profissional extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -7517913271635557041L;
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", insertable = false, updatable = false, unique = true)
    private UUID id;
    @Setter
    @Column(name = "nome", columnDefinition = "varchar(600)", nullable = false)
    private String nome;
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", columnDefinition = "varchar(50)", nullable = false)
    private Cargo cargo;
    @Setter
    @Column(name = "is_excluido", columnDefinition = "boolean", nullable = false)
    private boolean excluido = false;
    @Setter
    @Column(name = "nascimento", columnDefinition = "date", nullable = false, updatable = false)
    private LocalDate dataDeNascimento;

    public Profissional(String nome, Cargo cargo, LocalDate dataDeNascimento) {
        this.nome = nome;
        this.cargo = cargo;
        this.dataDeNascimento = dataDeNascimento;
    }
}
