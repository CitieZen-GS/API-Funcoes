package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tabela associativa N:N entre LembreteConsulta e Medicamento.
 */
@Entity
@Table(name = "T_Ajudante_familiar_Consulta_Medicamento")
@Getter
@Setter
@NoArgsConstructor
public class ConsultaMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta_medicamento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_lembrete_consulta", nullable = false)
    private LembreteConsulta lembreteConsulta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_medicamentos", nullable = false)
    private Medicamento medicamento;
}
