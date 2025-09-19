package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.service.DetalhesUsuarioService;
import Biblioteca_ung.projeto.service.LivroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(LivroController.class)
public class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    // Mocks de Segurança para o carregamento do contexto
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private DetalhesUsuarioService detalhesUsuarioService;

    // Apenas bibliotecários podem salvar um livro
    @Test
    @WithMockUser(roles = { "BIBLIOTECARIO" })
    void salvarLivro_comBibliotecario_chamaServiceERedireciona() throws Exception {
        // Arrange
        Livro livro = new Livro();
        livro.setTitulo("Livro Teste");

        when(livroService.salvar(any(Livro.class))).thenReturn(livro);

        // Act & Assert
        mockMvc.perform(post("/biblioteca/livro/salvar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("livro", livro)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bibliotecario"));

        verify(livroService, times(1)).salvar(any(Livro.class));
    }

    // Testa se um usuário comum é impedido de salvar um livro
    @Test
    @WithMockUser(roles = { "USUARIO" })
    void salvarLivro_comUsuarioComum_deveSerProibido() throws Exception {
        mockMvc.perform(post("/biblioteca/livro/salvar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                // REVERTIDO: A correção de segurança deve garantir o 403
                .andExpect(status().isForbidden()); // ESPERA 403 Forbidden

        verify(livroService, never()).salvar(any(Livro.class)); // Agora a verificação faz sentido
    }

    @Test
    @WithMockUser(roles = { "BIBLIOTECARIO" })
    void deletarLivro_comBibliotecario_chamaServiceERedireciona() throws Exception {
        // Arrange
        Long livroId = 5L;
        doNothing().when(livroService).deletar(livroId);

        // Act & Assert
        mockMvc.perform(post("/biblioteca/livro/deletar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", livroId.toString())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bibliotecario"));

        verify(livroService, times(1)).deletar(livroId);
    }
}