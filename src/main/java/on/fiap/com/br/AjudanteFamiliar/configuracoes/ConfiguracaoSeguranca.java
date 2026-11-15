package on.fiap.com.br.AjudanteFamiliar.configuracoes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Valida o JWT emitido pela API de autenticação (HS256, segredo compartilhado).
 * <p>
 * O token deve ter a claim "roles" com CUIDADOR ou FAMILIAR, ex.: {"sub": "1", "roles": ["CUIDADOR"]}.
 * <ul>
 *   <li>CUIDADOR: leitura e escrita (GET, POST, PUT, PATCH, DELETE)</li>
 *   <li>FAMILIAR: somente leitura (GET)</li>
 * </ul>
 */
@Configuration
public class ConfiguracaoSeguranca {

    @Value("${jwt.segredo}")
    private String segredo;

    @Bean
    public SecurityFilterChain filtroDeSeguranca(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(regras -> regras
                        .requestMatchers("/actuator/health", "/actuator/health/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("CUIDADOR", "FAMILIAR")
                        .anyRequest().hasRole("CUIDADOR"))
                .oauth2ResourceServer(servidor -> servidor
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(conversorDePerfis())));
        return http.build();
    }

    @Bean
    public JwtDecoder decodificadorJwt() {
        byte[] bytes = segredo.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("jwt.segredo precisa ter pelo menos 32 caracteres (256 bits) para HS256");
        }
        SecretKeySpec chave = new SecretKeySpec(bytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(chave).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /** Converte a claim "roles" em ROLE_CUIDADOR / ROLE_FAMILIAR. */
    private JwtAuthenticationConverter conversorDePerfis() {
        JwtGrantedAuthoritiesConverter perfis = new JwtGrantedAuthoritiesConverter();
        perfis.setAuthoritiesClaimName("roles");
        perfis.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
        conversor.setJwtGrantedAuthoritiesConverter(perfis);
        return conversor;
    }
}
