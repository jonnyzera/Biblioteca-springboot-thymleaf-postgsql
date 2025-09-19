package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.service.CustomUserDetails;
import Biblioteca_ung.projeto.service.EmprestimoService;
import Biblioteca_ung.projeto.service.LivroService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bibliotecario")
public class BibliotecarioController {

    private final LivroService livroService;
    private final EmprestimoService emprestimoService;

    // Injeção de dependência dos serviços necessários
    public BibliotecarioController(LivroService livroService, EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public String painelBibliotecario(Model model, Authentication authentication) {
        model.addAttribute("activePage", "bibliotecario");

        // 1. Carrega todos os dados necessários para as tabelas na tela
        // bibliotecario.html
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("emprestimos", emprestimoService.listarTodos());

        // 2. Extrai informações do usuário logado (necessário para o Dropdown do
        // Navbar)
        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuario = ((CustomUserDetails) authentication.getPrincipal()).getUsuario();
            model.addAttribute("usuario", usuario);
            model.addAttribute("userRole", usuario.getRole());
        }

        return "bibliotecario"; // Retorna o template bibliotecario.html
    }
}