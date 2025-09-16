package Biblioteca_ung.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

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
                        .requestMatchers("/biblioteca/perfil/form", "/biblioteca/perfil/salvar", "/css/**", "/js/**",
                                "/images/**", "/biblioteca/cadastro/login", "/")
                        .permitAll()
                        .requestMatchers("/biblioteca/livro/**", "/biblioteca/usuario/**").hasRole("BIBLIOTECARIO")
                        .requestMatchers("/biblioteca/emprestimo/**").hasAnyRole("USUARIO", "BIBLIOTECARIO")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/biblioteca/cadastro/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/biblioteca", true)
                        .failureUrl("/biblioteca/cadastro/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/biblioteca/cadastro/login")
                        .permitAll())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}