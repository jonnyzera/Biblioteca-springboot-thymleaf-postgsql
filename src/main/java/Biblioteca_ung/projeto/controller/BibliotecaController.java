<<<<<<< HEAD
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
=======
/*
 * package Biblioteca_ung.projeto.controller;
 * 
 * // import Biblioteca_ung.projeto.model.Livro;
 * // import Biblioteca_ung.projeto.model.Usuario;
 * import Biblioteca_ung.projeto.service.LivroService;
 * import Biblioteca_ung.projeto.service.UsuarioService;
 * import Biblioteca_ung.projeto.service.EmprestimoService;
 * 
 * import org.springframework.stereotype.Controller;
 * import org.springframework.ui.Model;
 * import org.springframework.web.bind.annotation.*;
 * 
 * @Controller
 * 
 * @RequestMapping("/biblioteca")
 * public class BibliotecaController {
 * 
 * private final LivroService livroService;
 * private final UsuarioService usuarioService;
 * private final EmprestimoService emprestimoService;
 * 
 * public BibliotecaController(LivroService livroService, UsuarioService
 * usuarioService,
 * EmprestimoService emprestimoService) {
 * this.livroService = livroService;
 * this.usuarioService = usuarioService;
 * this.emprestimoService = emprestimoService;
 * }
 * 
 * // ==================== TELA PRINCIPAL ====================
 * 
 * @GetMapping
 * public String telaBiblioteca(Model model) {
 * model.addAttribute("livros", livroService.listarTodos());
 * model.addAttribute("usuarios", usuarioService.listarTodos());
 * model.addAttribute("emprestimos", emprestimoService.listarTodos());
 * return "biblioteca"; // arquivo biblioteca.html
 * }
 * 
 * @GetMapping("/cadastro/login")
 * public String telaCadastroBiblioteca() {
 * return "cadastro-biblioteca"; // nome do arquivo em
 * src/main/resources/templates
 * }
 * }
 * 
 * /*
 * // ==================== CADASTRAR LIVRO ====================
 * 
 * @PostMapping("/livro")
 * public String salvarLivro(@ModelAttribute Livro livro) {
 * livroService.salvar(livro);
 * return "redirect:/biblioteca";
 * }
 * 
 * // ==================== CADASTRAR USUÁRIO ====================
 * 
 * @PostMapping("/usuario")
 * public String salvarUsuario(@ModelAttribute Usuario usuario) {
 * usuarioService.salvar(usuario);
 * return "redirect:/biblioteca";
 * }
 * 
 * // ==================== CRIAR EMPRÉSTIMO ====================
 * 
 * @PostMapping("/emprestimo")
 * public String salvarEmprestimo(@RequestParam Long usuarioId, @RequestParam
 * Long livroId) {
 * emprestimoService.criarEmprestimo(usuarioId, livroId);
 * return "redirect:/biblioteca";
 * }
 * 
 * 
 * }
 */
>>>>>>> 5da2da5a6b73c1f4d33fbe675d36431c6a2170dd
