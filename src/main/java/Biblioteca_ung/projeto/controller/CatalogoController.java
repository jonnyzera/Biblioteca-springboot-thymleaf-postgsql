package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.service.CustomUserDetails;
import Biblioteca_ung.projeto.service.EmprestimoService;
import Biblioteca_ung.projeto.service.LivroService;
import Biblioteca_ung.projeto.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private final LivroService livroService;
    private final EmprestimoService emprestimoService;

    public CatalogoController(LivroService livroService, EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public String exibirCatalogo(Model model, Authentication authentication) {
        model.addAttribute("activePage", "catalogo");

        if (authentication != null && authentication.isAuthenticated()) {
            // Acessa o objeto Usuario diretamente do principal
            Usuario usuario = ((CustomUserDetails) authentication.getPrincipal()).getUsuario();
            model.addAttribute("usuario", usuario);
            model.addAttribute("userRole", usuario.getRole());
            model.addAttribute("emprestimos", emprestimoService.listarTodos());
        }

        model.addAttribute("livros", livroService.listarTodos());
        List<String> categorias = livroService.listarTodos().stream()
                .map(Livro::getCategoria)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("categorias", categorias);

        return "catalogo";
    }
}