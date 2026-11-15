package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import on.fiap.com.br.AjudanteFamiliar.models.conversores.ConversorTipoAlimentacao;
import on.fiap.com.br.AjudanteFamiliar.models.enums.TipoAlimentacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "T_Ajudante_familiar_Alimentacao")
@Getter
@Setter
@NoArgsConstructor
public class Alimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alimentacao")
    private Long id;

    @Column(name = "nm_alimentacao", length = 20, nullable = false)
    private String nome;

    @Convert(converter = ConversorTipoAlimentacao.class)
    @Column(name = "tp_alimentacao", length = 20, nullable = false)
    private TipoAlimentacao tipo;

    @Column(name = "dt_consumo", nullable = false)
    private LocalDate dataConsumo;

    @Column(name = "hr_consumo", nullable = false)
    private LocalDateTime horaConsumo;

    @Column(name = "vlr_caloria", precision = 6, scale = 2)
    private BigDecimal calorias;

    @Column(name = "ds_preparacao", length = 100)
    private String preparacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_idoso", nullable = false)
    private Idoso idoso;
}
