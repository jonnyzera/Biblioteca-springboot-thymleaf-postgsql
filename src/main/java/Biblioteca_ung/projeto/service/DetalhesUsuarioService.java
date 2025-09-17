package Biblioteca_ung.projeto.service;

import java.util.Collections;
import java.util.List; // Importação correta para java.util.List

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User; // Importação correta para o User do Spring Security
import org.springframework.stereotype.Service;

import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.UsuarioRepository;

@Service
public class DetalhesUsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public DetalhesUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        // Cria e retorna um objeto UserDetails com a função (role) do usuário
        List<SimpleGrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().toUpperCase()));

        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}