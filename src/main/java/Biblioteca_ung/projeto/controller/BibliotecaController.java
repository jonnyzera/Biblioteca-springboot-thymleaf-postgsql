package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.service.EmprestimoService;
import Biblioteca_ung.projeto.service.LivroService;
import Biblioteca_ung.projeto.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaController {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final EmprestimoService emprestimoService;

    public BibliotecaController(LivroService livroService, UsuarioService usuarioService,
            EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.usuarioService = usuarioService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            String role = auth.getAuthorities().stream()
                    .filter(a -> a.getAuthority().startsWith("ROLE_"))
                    .findFirst()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .orElse("");
            model.addAttribute("userRole", role);
        }

        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "biblioteca";
    }
}