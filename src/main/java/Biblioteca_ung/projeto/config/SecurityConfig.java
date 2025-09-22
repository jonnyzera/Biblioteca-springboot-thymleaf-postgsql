package Biblioteca_ung.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// NOVOS IMPORTS
import Biblioteca_ung.projeto.service.CustomUserDetails;
import Biblioteca_ung.projeto.model.Usuario;

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

                                                // AGORA O ACESSO AO /bibliotecario É CONTROLADO NO CONTROLLER

                                                // REGRA CATCH-ALL
                                                .anyRequest().authenticated())
                                // ...
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                // Redirecionamento condicional
                                                .successHandler((request, response, authentication) -> {
                                                        // 1. Obter o tipo selecionado no formulário
                                                        String tipoSelecionado = request.getParameter("tipo");

                                                        // 2. Obter o usuário autenticado e seu Role real
                                                        CustomUserDetails userDetails = (CustomUserDetails) authentication
                                                                        .getPrincipal();
                                                        Usuario usuario = userDetails.getUsuario();
                                                        String roleReal = usuario.getRole(); // USUARIO ou BIBLIOTECARIO

                                                        // Converte o tipo selecionado para o formato da Role (ex:
                                                        // "usuario" -> "USUARIO")
                                                        String tipoSelecionadoUpper = tipoSelecionado != null
                                                                        ? tipoSelecionado.toUpperCase()
                                                                        : "";

                                                        // 3. Comparar as Roles: Se o usuário é Bibliotecário mas
                                                        // selecionou Usuário, ou vice-versa
                                                        if (!tipoSelecionadoUpper.equals(roleReal)) {
                                                                // Se o usuário logou com sucesso, mas selecionou a Role
                                                                // errada
                                                                response.sendRedirect("/login?role_error=true");
                                                                return;
                                                        }

                                                        // 4. Se as Roles coincidirem, prosseguir com o redirecionamento
                                                        if (roleReal.equals("BIBLIOTECARIO")) {
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