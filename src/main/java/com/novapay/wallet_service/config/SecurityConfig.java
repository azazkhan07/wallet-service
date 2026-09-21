    package com.novapay.wallet_service.config;

    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
    import org.springframework.security.oauth2.jwt.JwtDecoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
    import org.springframework.security.web.SecurityFilterChain;
    import javax.crypto.SecretKey;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import javax.crypto.spec.SecretKeySpec;
    import java.nio.charset.StandardCharsets;

    @Configuration
    @RequiredArgsConstructor
    @EnableWebSecurity
    public class SecurityConfig {

        private final InternalServiceAuthenticationFilter internalServiceAuthenticationFilter;
        @Value("${jwt.secret}")
        private String secret;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth

                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/actuator/health")
                            .permitAll()
                            .requestMatchers(HttpMethod.GET,
                                    "/api/v1/wallets/**")
                            .hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.POST,
                                    "/api/v1/wallets")
                            .hasAnyRole("ADMIN", "INTERNAL_SERVICE")
                            .requestMatchers(HttpMethod.POST,
                                    "/api/v1/wallets/credit",
                                    "/api/v1/wallets/debit")
                            .hasRole("ADMIN")
                            .anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 ->
                            oauth2.jwt(jwt ->
                                    jwt.jwtAuthenticationConverter(
                                            roleJwtAuthenticationConverter())))
                    .addFilterBefore(internalServiceAuthenticationFilter,
                            UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        public RoleJwtAuthenticationConverter roleJwtAuthenticationConverter() {
            return new RoleJwtAuthenticationConverter();
        }

        @Bean
        public JwtDecoder jwtDecoder() {

            SecretKey secretKey = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");
            return NimbusJwtDecoder
                    .withSecretKey(secretKey)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }
    }
