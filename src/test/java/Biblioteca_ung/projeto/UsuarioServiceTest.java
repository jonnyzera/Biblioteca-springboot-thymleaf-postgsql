package Biblioteca_ung.projeto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.UsuarioRepository;
import Biblioteca_ung.projeto.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Test User");
        usuario.setEmail("test@email.com");
        usuario.setSenha("senha123");
        usuario.setCpf("12345678901");
        usuario.setRole("USUARIO");
    }

    @Test
    void salvarUsuario_comSucesso() {
        when(passwordEncoder.encode(usuario.getSenha())).thenReturn("senha_criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario savedUser = usuarioService.salvar(usuario);

        assertNotNull(savedUser);
        assertEquals("senha_criptografada", savedUser.getSenha());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void salvarUsuario_comNomeVazio_lancaExcecao() {
        usuario.setNome(null);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.salvar(usuario);
        });

        assertEquals("Nome não pode ser vazio!", thrown.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}