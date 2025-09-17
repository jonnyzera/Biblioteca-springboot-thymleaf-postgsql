package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("biblioteca/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Livro livro) {
        livroService.salvar(livro);
        return "redirect:/bibliotecario";
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam Long id) {
        livroService.deletar(id);
        return "redirect:/bibliotecario";
    }
}
