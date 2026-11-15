package on.fiap.com.br.AjudanteFamiliar.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "LOGS")
@Getter
@Setter
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long id;

    @Column(name = "nm_procedure", length = 100)
    private String nomeProcedure;

    @Column(name = "nm_usuario", length = 50)
    private String nomeUsuario;

    @Column(name = "dt_ocorrencia")
    private LocalDateTime dataOcorrencia;

    @Column(name = "codigo_erro", length = 50)
    private String codigoErro;

    @Column(name = "mensagem_erro", length = 400)
    private String mensagemErro;
}
