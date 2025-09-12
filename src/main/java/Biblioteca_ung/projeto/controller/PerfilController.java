package Biblioteca_ung.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/biblioteca/perfil")
public class PerfilController {

    // Exibe o formulário (GET)
    @GetMapping("/form")
    public String mostrarFormulario() {
        return "cadastro-perfil"; // nome do arquivo HTML em src/main/resources/templates
    }

    // Recebe e processa o formulário (POST)
    @PostMapping("/salvar")
    public String salvarPerfil(@RequestParam String tipo,
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String novaSenha,
            @RequestParam String cpf,
            @RequestParam(required = false) String carteirinha,
            @RequestParam(required = false) String registro) {

        // Aqui você coloca a lógica para salvar no banco
        // Pode diferenciar se é bibliotecário ou usuário pelo campo "tipo"

        return "redirect:/login"; // redireciona para login depois de salvar
    }
}
