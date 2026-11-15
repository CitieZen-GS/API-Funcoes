package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import on.fiap.com.br.AjudanteFamiliar.models.conversores.ConversorStatusConsulta;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Ajudante_familiar_Lembrete_Consulta")
@Getter
@Setter
@NoArgsConstructor
public class LembreteConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lembrete_consulta")
    private Long id;

    @Column(name = "ds_titulo_lembrente_consulta", length = 20)
    private String titulo;

    @Column(name = "nm_consulta_medico", length = 20)
    private String nomeMedico;

    @Column(name = "dt_consulta")
    private LocalDate dataConsulta;

    @Column(name = "hr_consulta")
    private LocalDateTime horaConsulta;

    @Column(name = "ds_consulta", length = 100)
    private String descricao;

    /**
     * Inicia como PENDENTE: o DEFAULT do banco só vale quando a coluna é omitida
     * no INSERT, e o Hibernate sempre envia a coluna.
     */
    @Convert(converter = ConversorStatusConsulta.class)
    @Column(name = "st_consulta", length = 10, nullable = false)
    private StatusConsulta status = StatusConsulta.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_idoso", nullable = false)
    private Idoso idoso;

    @OneToMany(mappedBy = "lembreteConsulta")
    private List<ConsultaMedicamento> medicamentos = new ArrayList<>();
}
