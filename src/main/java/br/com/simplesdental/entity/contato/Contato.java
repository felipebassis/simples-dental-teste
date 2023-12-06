package br.com.simplesdental.entity.contato;

import br.com.simplesdental.entity.BaseEntity;
import br.com.simplesdental.entity.profissional.Profissional;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "contatos")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contato extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -2577368198617462585L;
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", insertable = false, updatable = false, unique = true)
    private UUID id;
    @Column(name = "nome", columnDefinition = "varchar(255)", nullable = false)
    private String nome;
    @Column(name = "contato", columnDefinition = "varchar(255)", nullable = false)
    private String contato;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false, updatable = false)
    private Profissional profissional;

    public Contato(String nome, String contato, Profissional profissional) {
        this.nome = nome;
        this.contato = contato;
        this.profissional = profissional;
    }
}

