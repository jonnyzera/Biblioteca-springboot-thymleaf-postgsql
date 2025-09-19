package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.service.DetalhesUsuarioService;
import Biblioteca_ung.projeto.service.EmprestimoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(EmprestimoController.class)
public class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmprestimoService emprestimoService;

    // Mocks de Segurança e Dependências para o carregamento do contexto
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private DetalhesUsuarioService detalhesUsuarioService;

    // Testa o empréstimo feito por um USUARIO
    @Test
    @WithMockUser(username = "usuario@test.com", roles = { "USUARIO" })
    void emprestar_comUsuarioLogado_chamaServiceERedireciona() throws Exception {
        // Arrange
        Long usuarioId = 1L;
        Long livroId = 10L;
        doNothing().when(emprestimoService).criarEmprestimo(usuarioId, livroId);

        // Act & Assert
        mockMvc.perform(post("/biblioteca/emprestimo/emprestar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("usuarioId", usuarioId.toString())
                .param("livroId", livroId.toString())
                .with(csrf())) // Adiciona o token CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalogo"));

        // Verifica se a lógica de negócio foi chamada
        verify(emprestimoService, times(1)).criarEmprestimo(usuarioId, livroId);
    }

    // Testa a devolução feita por um BIBLIOTECARIO
    @Test
    @WithMockUser(username = "biblio@test.com", roles = { "BIBLIOTECARIO" })
    void devolver_comBibliotecarioLogado_chamaServiceERedireciona() throws Exception {
        // Arrange
        Long emprestimoId = 50L;
        // O método devolverEmprestimo no service deleta o empréstimo, então mockamos o
        // deleteById
        doNothing().when(emprestimoService).deletar(emprestimoId);

        // Act & Assert
        mockMvc.perform(post("/biblioteca/emprestimo/devolver")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("emprestimoId", emprestimoId.toString())
                .with(csrf())) // Adiciona o token CSRF
                .andExpect(status().is3xxRedirection())
                // O Controller retorna "biblioteca/bibliotecario"
                .andExpect(redirectedUrl("biblioteca/bibliotecario"));

        // Verifica se a lógica de negócio foi chamada
        verify(emprestimoService, times(1)).deletar(emprestimoId);
    }

    // Testa se um usuário comum não pode realizar a devolução
    @Test
    @WithMockUser(roles = { "USUARIO" })
    void devolver_comUsuarioComum_deveSerProibido() throws Exception {
        // O método de devolução deve ser protegido, assumindo que
        // /biblioteca/emprestimo/**
        // não é explicitamente liberado para USUARIO no SecurityConfig.
        mockMvc.perform(post("/biblioteca/emprestimo/devolver")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("emprestimoId", "1")
                .with(csrf()))
                .andExpect(status().isForbidden()); // Espera 403 Forbidden

        verify(emprestimoService, never()).deletar(anyLong());
    }
}