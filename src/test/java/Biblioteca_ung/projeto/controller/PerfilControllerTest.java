package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.dto.PerfilCadastroDTO;
import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.UsuarioRepository;
import Biblioteca_ung.projeto.service.DetalhesUsuarioService;
import Biblioteca_ung.projeto.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.boot.test.mock.mockito.MockBean; // Permite Mocks no contexto Spring

// Testa o PerfilController isolando a camada Web (sem carregar todo o contexto)
@WebMvcTest(PerfilController.class)
public class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto para simular requisições HTTP

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    // NOVOS MOCKS para satisfazer o Spring Security e evitar falha no contexto
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private DetalhesUsuarioService detalhesUsuarioService;

    private PerfilCadastroDTO criarDtoValido() {
        PerfilCadastroDTO dto = new PerfilCadastroDTO();
        dto.setNome("Novo Usuário");
        dto.setEmail("novo@email.com");
        dto.setSenha("senha123");
        dto.setNovaSenha("senha123"); // Confirmação igual
        dto.setCpf("12345678901");
        dto.setTipo("usuario");
        return dto;
    }

    @Test
    void salvarPerfil_comDadosValidos_redirecionaParaLogin() throws Exception {
        // Arrange
        PerfilCadastroDTO dto = criarDtoValido();
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/biblioteca/perfil/salvar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("perfilCadastroDTO", dto)
                .with(csrf())) // CORREÇÃO: Adicionando token CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(usuarioService, times(1)).salvar(any(Usuario.class));
    }

    @Test
    void salvarPerfil_emailJaEmUso_retornaErroParaFormulario() throws Exception {
        // Arrange
        PerfilCadastroDTO dto = criarDtoValido();
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/biblioteca/perfil/salvar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("perfilCadastroDTO", dto)
                .with(csrf())) // CORREÇÃO: Adicionando token CSRF
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro-perfil"))
                .andExpect(model().attributeHasFieldErrors("perfilCadastroDTO", "email"));

        verify(usuarioService, never()).salvar(any(Usuario.class));
    }

    @Test
    void salvarPerfil_senhasNaoCoincidem_retornaErroParaFormulario() throws Exception {
        // Arrange
        PerfilCadastroDTO dto = criarDtoValido();
        dto.setNovaSenha("senhaDiferente");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/biblioteca/perfil/salvar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("perfilCadastroDTO", dto)
                .with(csrf())) // CORREÇÃO: Adicionando token CSRF
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro-perfil"))
                .andExpect(model().attributeHasFieldErrors("perfilCadastroDTO", "novaSenha"));

        verify(usuarioService, never()).salvar(any(Usuario.class));
    }

    @Test
    void salvarPerfil_cpfJaEmUso_retornaErroParaFormulario() throws Exception {
        // Arrange
        PerfilCadastroDTO dto = criarDtoValido();
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(dto.getCpf())).thenReturn(true); // Simula CPF já em uso

        // Act & Assert
        mockMvc.perform(post("/biblioteca/perfil/salvar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("perfilCadastroDTO", dto)
                .with(csrf())) // CORREÇÃO: Adicionando token CSRF
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro-perfil"))
                .andExpect(model().attributeHasFieldErrors("perfilCadastroDTO", "cpf"));

        verify(usuarioService, never()).salvar(any(Usuario.class));
    }
}