package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Ajudante_familiar_Idoso")
@Getter
@Setter
@NoArgsConstructor
public class Idoso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_idoso")
    private Long id;

    @Column(name = "nm_idoso", length = 20, nullable = false)
    private String nome;

    @Column(name = "cpf_idoso", length = 14)
    private String cpf;

    @Column(name = "vlr_altura", precision = 3, scale = 2)
    private BigDecimal altura;

    @Column(name = "vlr_peso", precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "ds_comportamento", length = 100)
    private String comportamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuidador", nullable = false)
    private Cuidador cuidador;

    @OneToMany(mappedBy = "idoso")
    private List<Parente> parentes = new ArrayList<>();

    @OneToMany(mappedBy = "idoso")
    private List<Doenca> doencas = new ArrayList<>();

    @OneToMany(mappedBy = "idoso")
    private List<Atividade> atividades = new ArrayList<>();

    @OneToMany(mappedBy = "idoso")
    private List<Alimentacao> alimentacoes = new ArrayList<>();

    @OneToMany(mappedBy = "idoso")
    private List<LembreteConsulta> lembretesConsulta = new ArrayList<>();

    @OneToMany(mappedBy = "idoso")
    private List<Medicamento> medicamentos = new ArrayList<>();
}
