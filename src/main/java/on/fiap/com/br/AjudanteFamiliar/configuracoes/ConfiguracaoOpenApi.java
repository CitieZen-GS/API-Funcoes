package on.fiap.com.br.AjudanteFamiliar.configuracoes;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Documentação Swagger / OpenAPI.
 * <ul>
 *   <li>Swagger UI: /swagger-ui.html</li>
 *   <li>JSON OpenAPI: /v3/api-docs</li>
 * </ul>
 * O botão "Authorize" recebe o JWT (sem o prefixo "Bearer").
 */
@Configuration
public class ConfiguracaoOpenApi {

    private static final String ESQUEMA_JWT = "bearerAuth";

    @Bean
    public OpenAPI documentacaoApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ajudante Familiar: API de Funções")
                        .version("v1")
                        .description("""
                                Funções do app de cuidados de idosos: cadastro de medicamentos, atividades,                                 alimentação, doenças, lembretes de consulta e busca de dados do idoso.

                                **Perfis (claim `roles` do JWT):**
                                - `CUIDADOR`: leitura e escrita
                                - `FAMILIAR`: somente leitura (GET)

                                Datas no formato `aaaa-mm-dd` e horas no formato `hh:mm`."""))
                .components(new Components().addSecuritySchemes(ESQUEMA_JWT, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Token emitido pela API de autenticação")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_JWT));
    }
}
