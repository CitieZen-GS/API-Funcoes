package on.fiap.com.br.AjudanteFamiliar;

import org.springframework.boot.SpringApplication;

public class TestAjudanteFamiliarApplication {

	public static void main(String[] args) {
		SpringApplication.from(AjudanteFamiliarApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
