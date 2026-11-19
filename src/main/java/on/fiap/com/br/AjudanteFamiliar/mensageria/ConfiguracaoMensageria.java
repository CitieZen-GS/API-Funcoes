package on.fiap.com.br.AjudanteFamiliar.mensageria;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Estrutura do RabbitMQ usada pela API de funções (consumidora de eventos).
 * <pre>
 * [autenticação] --"autenticacao.acesso-revogado"--> ajudante-familiar.eventos (topic)
 *                                                       └─> funcoes.acesso-revogado  (fila desta API)
 *                                     falhou 3 vezes ──> ajudante-familiar.eventos.dlx
 *                                                       └─> funcoes.acesso-revogado.dlq (para análise)
 * </pre>
 * O Spring declara tudo isso no RabbitMQ ao conectar (se já existir, nada muda).
 */
@Configuration
public class ConfiguracaoMensageria {

    /** Exchange compartilhado por todas as APIs. */
    public static final String EXCHANGE_EVENTOS = "ajudante-familiar.eventos";

    /** Para onde vão as mensagens que falharam em todas as tentativas (dead letter exchange). */
    public static final String EXCHANGE_FALHAS = "ajudante-familiar.eventos.dlx";

    public static final String ROTA_ACESSO_REVOGADO = "autenticacao.acesso-revogado";

    /** Filas desta API: "<api que consome>.<evento>". */
    public static final String FILA_ACESSO_REVOGADO = "funcoes.acesso-revogado";
    public static final String FILA_ACESSO_REVOGADO_FALHAS = FILA_ACESSO_REVOGADO + ".dlq";

    @Bean
    public TopicExchange exchangeEventos() {
        return new TopicExchange(EXCHANGE_EVENTOS, true, false);
    }

    @Bean
    public TopicExchange exchangeFalhas() {
        return new TopicExchange(EXCHANGE_FALHAS, true, false);
    }

    /** Fila durável: se esta API estiver fora do ar, as mensagens esperam aqui. */
    @Bean
    public Queue filaAcessoRevogado() {
        return QueueBuilder.durable(FILA_ACESSO_REVOGADO)
                .deadLetterExchange(EXCHANGE_FALHAS)
                .build();
    }

    @Bean
    public Queue filaAcessoRevogadoFalhas() {
        return QueueBuilder.durable(FILA_ACESSO_REVOGADO_FALHAS).build();
    }

    @Bean
    public Binding ligacaoAcessoRevogado() {
        return BindingBuilder.bind(filaAcessoRevogado()).to(exchangeEventos()).with(ROTA_ACESSO_REVOGADO);
    }

    @Bean
    public Binding ligacaoAcessoRevogadoFalhas() {
        return BindingBuilder.bind(filaAcessoRevogadoFalhas()).to(exchangeFalhas()).with(ROTA_ACESSO_REVOGADO);
    }

    /** Mensagens em JSON; o tipo é deduzido pelo parâmetro do método @RabbitListener. */
    @Bean
    public MessageConverter conversorJson() {
        return new JacksonJsonMessageConverter();
    }
}
