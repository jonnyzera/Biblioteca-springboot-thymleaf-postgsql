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
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // NOVO IMPORT

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
    public String painelBibliotecario(Model model, Authentication authentication, RedirectAttributes ra) {

        // Se por algum motivo o usuário não estiver autenticado (improvável, mas
        // seguro)
        if (authentication == null || !authentication.isAuthenticated()) {
            ra.addFlashAttribute("erro", "Acesso negado. Você precisa estar logado.");
            return "redirect:/login";
        }

        Usuario usuario = ((CustomUserDetails) authentication.getPrincipal()).getUsuario();

        // VERIFICAÇÃO DE PERMISSÃO (NOVO)
        if (!usuario.getRole().equals("BIBLIOTECARIO")) {
            // Se não for Bibliotecário, redireciona com mensagem de erro amigável
            ra.addFlashAttribute("erro", "Acesso negado. Apenas Bibliotecários podem acessar a área de gerenciamento.");
            return "redirect:/catalogo";
        }
        // FIM DA VERIFICAÇÃO

        model.addAttribute("activePage", "bibliotecario");

        // 1. Carrega todos os dados necessários
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("emprestimos", emprestimoService.listarTodos());

        // 2. Extrai informações do usuário logado
        model.addAttribute("usuario", usuario);
        model.addAttribute("userRole", usuario.getRole());

        return "bibliotecario"; // Retorna o template bibliotecario.html
    }
}