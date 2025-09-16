/*
 * package Biblioteca_ung.projeto.service;
 * 
 * import java.util.List;
 * 
 * import org.apache.catalina.User;
 * import org.springframework.stereotype.Service;
 * 
 * import Biblioteca_ung.projeto.repository.UsuarioRepository;
 * 
 * @Service
 * public class CustomUserDetailsService implements UserDetailsService {
 * 
 * private final UsuarioRepository usuarioRepository;
 * 
 * public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
 * this.usuarioRepository = usuarioRepository;
 * }
 * 
 * @Override
 * public UserDetails loadUserByUsername(String email) throws
 * UsernameNotFoundException {
 * Usuario u = usuarioRepository.findByEmail(email.toLowerCase().trim())
 * .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
 * // Sem roles/authorities no exemplo; poderia carregar da entidade
 * return User.withUsername(u.getEmail())
 * .password(u.getSenhaHash())
 * .authorities(List.of()) // ou "ROLE_USER"
 * .build();
 * }
 * }
 */