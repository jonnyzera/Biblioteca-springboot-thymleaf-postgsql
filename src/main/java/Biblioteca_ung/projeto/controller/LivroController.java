package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.service.LivroService;
import org.springframework.ui.Model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("biblioteca/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livro", livroService.listarTodos());
        return "biblioteca"; // exibe biblioteca.html
    }

    @PostMapping("/salvar")
    @ResponseBody
    public Livro salvar(@ModelAttribute Livro livro) {
        return livroService.salvar(livro); // salva e retorna o livro salvo
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build().toString(); // HTTP 204 No Content
    }
}
