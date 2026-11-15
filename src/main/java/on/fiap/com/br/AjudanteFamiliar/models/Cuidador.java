package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Ajudante_familiar_Cuidador")
@Getter
@Setter
@NoArgsConstructor
public class Cuidador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuidador")
    private Long id;

    @Column(name = "nm_cuidador", length = 20, nullable = false)
    private String nome;

    @Column(name = "cpf_cuidador", length = 14, unique = true)
    private String cpf;

    @Column(name = "ds_email", length = 100, unique = true)
    private String email;

    @Column(name = "ds_senha", length = 100)
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @OneToMany(mappedBy = "cuidador")
    private List<Idoso> idosos = new ArrayList<>();
}
