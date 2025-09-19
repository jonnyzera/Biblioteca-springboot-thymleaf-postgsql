package Biblioteca_ung.projeto.controller;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // NOVO IMPORT

@Controller
@RequestMapping("biblioteca/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Livro livro,
            @RequestParam(value = "capaFile", required = false) MultipartFile capaFile,
            @RequestParam(value = "capaMethod", required = false) String capaMethod,
            RedirectAttributes ra // NOVO PARÂMETRO
    ) {
        if ("upload".equals(capaMethod) && capaFile != null && !capaFile.isEmpty()) {
            livro.setUrlCapa(null);
        }

        try {
            livroService.salvar(livro);
            ra.addFlashAttribute("sucesso", "Livro '" + livro.getTitulo() + "' salvo com sucesso!");
            return "redirect:/bibliotecario";
        } catch (IllegalArgumentException e) {
            // Captura exceções como "Título não pode ser vazio!"
            ra.addFlashAttribute("erro", e.getMessage());
            ra.addFlashAttribute("livro", livro); // Envia o objeto de volta para preencher o formulário
            return "redirect:/bibliotecario";
        } catch (Exception e) {
            // Captura outros erros inesperados
            ra.addFlashAttribute("erro", "Erro interno ao salvar livro: " + e.getMessage());
            return "redirect:/bibliotecario";
        }
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam Long id) {
        livroService.deletar(id);
        return "redirect:/bibliotecario";
    }
}