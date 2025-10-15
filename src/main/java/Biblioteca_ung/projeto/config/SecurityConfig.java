package Biblioteca_ung.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                                                // Rotas públicas (sem necessidade de login)
                                                .requestMatchers("/", "/login", "/biblioteca/perfil/form",
                                                                "/biblioteca/perfil/salvar", "/css/**",
                                                                "/js/**", "/imagens/**", "/catalogo")
                                                .permitAll()

                                                // REGRAS RESTRITIVAS (do mais específico para o mais geral)
                                                // Permite que qualquer usuário autenticado veja os detalhes do livro
                                                .requestMatchers("/biblioteca/livro/detalhes/{id}").authenticated()

                                                // Rotas para Empréstimos (acesso liberado para Bibliotecário para
                                                // que o Controller possa redirecioná-lo)
                                                .requestMatchers("/emprestimos").hasAnyRole("USUARIO", "BIBLIOTECARIO")

                                                // Rotas para USUARIO
                                                .requestMatchers("/biblioteca/emprestimo/emprestar").hasRole("USUARIO")

                                                // Rotas para BIBLIOTECARIO
                                                .requestMatchers("/bibliotecario").hasRole("BIBLIOTECARIO")
                                                .requestMatchers("/biblioteca/emprestimo/devolver")
                                                .hasRole("BIBLIOTECARIO")

                                                // Todas as outras rotas que começam com /biblioteca/livro são restritas
                                                // a BIBLIOTECARIO
                                                .requestMatchers("/biblioteca/livro/**").hasRole("BIBLIOTECARIO")

                                                // Rotas de perfil que requerem apenas autenticação (qualquer role)
                                                .requestMatchers("/biblioteca/perfil",
                                                                "/biblioteca/perfil/editar",
                                                                "/biblioteca/perfil/salvar-edicao")
                                                .authenticated()

                                                // Todas as outras requisições que não se encaixam nas regras acima
                                                // requerem autenticação
                                                .anyRequest().authenticated())
                                // ...
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                // Redirecionamento condicional simplificado
                                                .successHandler((request, response, authentication) -> {
                                                        CustomUserDetails userDetails = (CustomUserDetails) authentication
                                                                        .getPrincipal();
                                                        Usuario usuario = userDetails.getUsuario();
                                                        String roleReal = usuario.getRole();

                                                        // Ação Principal: Redirecionar diretamente para a área do
                                                        // Bibliotecário
                                                        if (roleReal.equals("BIBLIOTECARIO")) {
                                                                response.sendRedirect("/bibliotecario");
                                                        } else {
                                                                // Redirecionamento padrão para o Usuário
                                                                response.sendRedirect("/catalogo");
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
