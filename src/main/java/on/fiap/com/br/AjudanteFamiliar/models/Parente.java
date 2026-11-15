package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "T_Ajudante_familiar_Parente")
@Getter
@Setter
@NoArgsConstructor
public class Parente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parente")
    private Long id;

    @Column(name = "nm_parente", length = 20)
    private String nome;

    @Column(name = "nr_telefone", length = 18, unique = true)
    private String telefone;

    @Column(name = "ds_email", length = 100, unique = true)
    private String email;

    @Column(name = "ds_senha", length = 100)
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_idoso", nullable = false)
    private Idoso idoso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;
}
