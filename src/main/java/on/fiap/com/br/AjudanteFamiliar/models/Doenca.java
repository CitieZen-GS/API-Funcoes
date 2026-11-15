package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_Ajudante_familiar_Doencas")
@Getter
@Setter
@NoArgsConstructor
public class Doenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doenca")
    private Long id;

    @Column(name = "nm_doenca", length = 20)
    private String nome;

    @Column(name = "ds_doenca", length = 20)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_idoso", nullable = false)
    private Idoso idoso;
}
