package on.fiap.com.br.AjudanteFamiliar;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	OracleContainer oracleFreeContainer() {
		// "slim-faststart": o banco já vem criado na imagem e sobe em segundos ("latest" costuma
		// passar de 1 minuto, o tempo padrão do Testcontainers, e quebrar o teste e o CI)
		return new OracleContainer(DockerImageName.parse("gvenzl/oracle-free:23-slim-faststart"))
				.withStartupTimeout(Duration.ofMinutes(5));
	}

	@Bean
	@ServiceConnection
	RabbitMQContainer rabbitContainer() {
		return new RabbitMQContainer(DockerImageName.parse("rabbitmq:latest"));
	}

}
