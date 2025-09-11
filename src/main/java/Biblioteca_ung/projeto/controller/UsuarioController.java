package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.service.UsuarioService;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/biblioteca/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "biblioteca"; // Retorna a página da biblioteca
    }

    @PostMapping("/salvar-usuario")
    public String salvar(@ModelAttribute Usuario usuario) {
        usuarioService.salvar(usuario);
        return "biblioteca";
    }

    @PostMapping("/deletar-usuario")
    public String deletar(@RequestParam Long id) {
        usuarioService.deletar(id);
        return "biblioteca";
    }
}
