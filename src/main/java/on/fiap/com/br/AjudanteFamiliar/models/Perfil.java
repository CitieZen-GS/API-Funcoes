package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Ajudante_familiar_Perfil")
@Getter
@Setter
@NoArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long id;

    @Column(name = "nm_perfil", length = 20, nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cargo", nullable = false)
    private Cargo cargo;

    @OneToMany(mappedBy = "perfil")
    private List<Cuidador> cuidadores = new ArrayList<>();

    @OneToMany(mappedBy = "perfil")
    private List<Parente> parentes = new ArrayList<>();
}
