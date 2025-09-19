package Biblioteca_ung.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/login", "/biblioteca/perfil/form",
                                                                "/biblioteca/perfil/salvar", "/css/**",
                                                                "/js/**", "/imagens/**", "/catalogo")
                                                .permitAll()

                                                // REGRAS RESTRITIVAS
                                                .requestMatchers("/biblioteca/livro/**").hasRole("BIBLIOTECARIO")
                                                .requestMatchers("/bibliotecario").hasRole("BIBLIOTECARIO")

                                                // REGRA CATCH-ALL
                                                .anyRequest().authenticated())
                                // ...
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                // Redirecionamento condicional
                                                .successHandler((request, response, authentication) -> {
                                                        Collection<? extends GrantedAuthority> authorities = authentication
                                                                        .getAuthorities();
                                                        boolean isBibliotecario = authorities.stream()
                                                                        .anyMatch(a -> a.getAuthority()
                                                                                        .equals("ROLE_BIBLIOTECARIO"));

                                                        if (isBibliotecario) {
                                                                response.sendRedirect("/bibliotecario"); // Bibliotecário
                                                        } else {
                                                                response.sendRedirect("/catalogo"); // Usuário
                                                        }
                                                })
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .permitAll())
                                .csrf(csrf -> csrf.disable());
                return http.build();
        }
}