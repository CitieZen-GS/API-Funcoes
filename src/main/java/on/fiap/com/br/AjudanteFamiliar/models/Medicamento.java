package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Ajudante_familiar_Medicamentos")
@Getter
@Setter
@NoArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamentos")
    private Long id;

    @Column(name = "nm_medicamentos", length = 20)
    private String nome;

    @Column(name = "via_administracao", length = 20)
    private String viaAdministracao;

    @Column(name = "vl_frquencia", length = 50)
    private String frequencia;

    @Column(name = "ds_instrucao_uso", length = 100)
    private String instrucaoUso;

    @Column(name = "dt_medicamento")
    private LocalDate data;

    @Column(name = "hr_medicamento", nullable = false)
    private LocalDateTime hora;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_idoso", nullable = false)
    private Idoso idoso;

    @OneToMany(mappedBy = "medicamento")
    private List<ConsultaMedicamento> consultas = new ArrayList<>();
}
