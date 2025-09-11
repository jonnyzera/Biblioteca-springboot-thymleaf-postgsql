package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.service.EmprestimoService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/biblioteca/emprestimo")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimo", emprestimoService.listarTodos());
        return "biblioteca"; // Retorna a página da biblioteca
    }

    @PostMapping("/emprestar")
    public String emprestar(@RequestParam Long usuarioId, @RequestParam Long livroId) {
        emprestimoService.criarEmprestimo(usuarioId, livroId);
        return "redirect:/biblioteca/emprestimo";
    }

    @PostMapping("/devolver")
    public String devolver(@RequestParam Long emprestimoId) {
        emprestimoService.deletar(emprestimoId);
        return "biblioteca";
    }
}