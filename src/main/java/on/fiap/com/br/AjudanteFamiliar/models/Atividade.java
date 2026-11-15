package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_Ajudante_familiar_Atividades")
@Getter
@Setter
@NoArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long id;

    @Column(name = "nm_atividade", length = 20, nullable = false)
    private String nome;

    @Column(name = "ds_atividade", length = 80)
    private String descricao;

    @Column(name = "dt_atividade", nullable = false)
    private LocalDate data;

    /** Oracle DATE guarda data e hora, por isso LocalDateTime. */
    @Column(name = "hr_atividade_inicio")
    private LocalDateTime horaInicio;

    @Column(name = "hr_atividade_fim")
    private LocalDateTime horaFim;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_idoso", nullable = false)
    private Idoso idoso;
}
