package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.service.LivroService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("biblioteca/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    // NOVO MÉTODO: Exibe a página de detalhes de um único livro
    @GetMapping("/detalhes/{id}")
    public String exibirDetalhesLivro(@PathVariable Long id, Model model) {
        Livro livro = livroService.buscarPorId(id);
        if (livro != null) {
            model.addAttribute("livro", livro);
            return "livro-detalhes"; // Retorna o nome do novo template
        }
        // Se o livro não for encontrado, redireciona para o catálogo com erro
        return "redirect:/catalogo?erro=Livro não encontrado.";
    }

    // Endpoint para buscar um livro por ID e retornar como JSON
    @GetMapping("/detalhes-api/{id}")
    @ResponseBody
    public Livro getLivroDetalhes(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Livro livro,
            @RequestParam(value = "capaFile", required = false) MultipartFile capaFile,
            @RequestParam(value = "capaMethod", required = false) String capaMethod,
            RedirectAttributes ra) {
        if ("upload".equals(capaMethod) && capaFile != null && !capaFile.isEmpty()) {
            livro.setUrlCapa(null);
        }

        try {
            livroService.salvar(livro);
            ra.addFlashAttribute("sucesso", "Livro '" + livro.getTitulo() + "' salvo com sucesso!");
            return "redirect:/bibliotecario";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            ra.addFlashAttribute("livro", livro);
            return "redirect:/bibliotecario";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro interno ao salvar livro: " + e.getMessage());
            return "redirect:/bibliotecario";
        }
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam Long id, RedirectAttributes ra) {
        try {
            livroService.deletar(id);
            ra.addFlashAttribute("sucesso", "Livro excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", "Ainda há empréstimos para este livro. Não é possível deletá-lo.");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao tentar deletar o livro: " + e.getMessage());
        }
        return "redirect:/bibliotecario";
    }
}