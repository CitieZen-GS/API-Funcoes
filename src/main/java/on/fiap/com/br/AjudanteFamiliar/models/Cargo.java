package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import on.fiap.com.br.AjudanteFamiliar.models.conversores.ConversorSimNao;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Ajudante_familiar_Cargo")
@Getter
@Setter
@NoArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Long id;

    @Column(name = "nm_cargo", length = 20)
    private String nome;

    /** Coluna fg_permissao: 'S' = true, 'N' = false. */
    @Convert(converter = ConversorSimNao.class)
    @Column(name = "fg_permissao", length = 1)
    private Boolean permissao;

    @OneToMany(mappedBy = "cargo")
    private List<Perfil> perfis = new ArrayList<>();
}
