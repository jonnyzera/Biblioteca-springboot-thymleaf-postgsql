package Biblioteca_ung.projeto.service;

import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalhesUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DetalhesUsuarioService detalhesUsuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("user@test.com");
        usuario.setSenha("senha_cripto");
        usuario.setRole("USUARIO");
    }

    @Test
    void loadUserByUsername_usuarioEncontrado_retornaUserDetails() {
        // Arrange
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = detalhesUsuarioService.loadUserByUsername("user@test.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        // Verifica se a Authority (ROLE_USUARIO) foi carregada corretamente
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO")));
    }

    @Test
    void loadUserByUsername_usuarioNaoEncontrado_lancaUsernameNotFoundException() {
        // Arrange
        String emailInexistente = "naoexiste@test.com";
        when(usuarioRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

        // Act & Assert: Verifica se a exceção correta de segurança é lançada
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            detalhesUsuarioService.loadUserByUsername(emailInexistente);
        });

        assertTrue(thrown.getMessage().contains("Usuário não encontrado"));
    }
}