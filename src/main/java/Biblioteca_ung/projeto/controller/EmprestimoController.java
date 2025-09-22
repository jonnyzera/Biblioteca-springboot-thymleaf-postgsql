package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Usuario; // NOVO IMPORT
import Biblioteca_ung.projeto.service.CustomUserDetails; // NOVO IMPORT
import Biblioteca_ung.projeto.service.EmprestimoService;
import org.springframework.security.core.Authentication; // NOVO IMPORT
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
// REMOVIDO: @RequestMapping("/biblioteca/emprestimo")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    // NOVO MÉTODO: Mapeia o link do header "/emprestimos"
    @GetMapping("/emprestimos")
    public String listarEmprestimos(Model model, Authentication authentication) {
        model.addAttribute("activePage", "emprestimos");

        // 1. Carregar dados do usuário logado (necessário para o Navbar e para filtrar)
        Usuario usuario = ((CustomUserDetails) authentication.getPrincipal()).getUsuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("userRole", usuario.getRole());

        // Carregar empréstimos para exibição no menu de perfil (dropdown)
        model.addAttribute("emprestimos", emprestimoService.listarPorUsuario(usuario));

        if (usuario.getRole().equals("BIBLIOTECARIO")) {
            // Redireciona Bibliotecário para o painel de gerenciamento
            return "redirect:/bibliotecario";
        } else {
            // Se for USUARIO, carrega a lista para o template
            model.addAttribute("meusEmprestimos", emprestimoService.listarPorUsuario(usuario));
            return "emprestimos-usuario"; // Renderiza o template do usuário
        }
    }

    // Método original, ajustado com o caminho completo /biblioteca/emprestimo
    @GetMapping("/biblioteca/emprestimo")
    public String listar(Model model) {
        model.addAttribute("emprestimo", emprestimoService.listarTodos());
        return "biblioteca";
    }

    // Método de empréstimo, ajustado com o caminho completo
    @PostMapping("/biblioteca/emprestimo/emprestar")
    public String emprestar(@RequestParam Long usuarioId, @RequestParam Long livroId) {
        emprestimoService.criarEmprestimo(usuarioId, livroId);
        return "redirect:/catalogo";
    }

    // Método de devolução, ajustado com o caminho completo e usando redirect para
    // POST
    @PostMapping("/biblioteca/emprestimo/devolver")
    public String devolver(@RequestParam Long emprestimoId) {
        emprestimoService.devolverEmprestimo(emprestimoId);
        return "redirect:/bibliotecario";
    }
}