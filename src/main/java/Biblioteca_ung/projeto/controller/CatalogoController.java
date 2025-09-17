package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.model.Usuario;
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

/*
 * @Controller
 * 
 * @RequestMapping("/catalogo")
 * public class CatalogoController {
 * 
 * private final LivroService livroService;
 * private final UsuarioService usuarioService;
 * private final EmprestimoService emprestimoService;
 * 
 * public CatalogoController(LivroService livroService, UsuarioService
 * usuarioService,
 * EmprestimoService emprestimoService) {
 * this.livroService = livroService;
 * this.usuarioService = usuarioService;
 * this.emprestimoService = emprestimoService;
 * }
 * 
 * @GetMapping
 * public String exibirCatalogo(Model model, Authentication authentication) {
 * String userEmail = authentication.getName();
 * Usuario usuario = usuarioService.buscarPorEmail(userEmail);
 * 
 * // Adiciona informações do usuário logado ao modelo
 * if (usuario != null) {
 * model.addAttribute("usuario", usuario);
 * model.addAttribute("userRole", usuario.getRole());
 * model.addAttribute("emprestimos", emprestimoService.listarTodos());
 * }
 * 
 * model.addAttribute("livros", livroService.listarTodos());
 * 
 * List<String> categorias = livroService.listarTodos().stream()
 * .map(Livro::getCategoria)
 * .distinct()
 * .collect(Collectors.toList());
 * model.addAttribute("categorias", categorias);
 * 
 * // Adiciona a página ativa ao modelo
 * model.addAttribute("activePage", "catalogo");
 * 
 * return "catalogo";
 * }
 * }
 */

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final EmprestimoService emprestimoService;

    public CatalogoController(LivroService livroService, UsuarioService usuarioService,
            EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.usuarioService = usuarioService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public String exibirCatalogo(Model model, Authentication authentication) {
        // Adiciona a página ativa para o template de navegação
        model.addAttribute("activePage", "catalogo");

        // Verifica se o usuário está autenticado antes de tentar acessar seus dados
        if (authentication != null && authentication.isAuthenticated()) {
            String userEmail = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(userEmail);
            model.addAttribute("usuario", usuario);
            if (usuario != null) {
                model.addAttribute("userRole", usuario.getRole());
                model.addAttribute("emprestimos", emprestimoService.listarTodos());
            }
        }
        // Adiciona a lista de livros e categorias, que são públicas
        model.addAttribute("livros", livroService.listarTodos());
        List<String> categorias = livroService.listarTodos().stream()
                .map(Livro::getCategoria)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("categorias", categorias);

        return "catalogo";
    }
}