package Biblioteca_ung.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import Biblioteca_ung.projeto.dto.PerfilCadastroDTO;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("perfilCadastroDTO", new PerfilCadastroDTO());
        return "login-biblioteca";
    }

    // NOVO: Mapeamento para carregar o arquivo principal.html
    @GetMapping("/principal")
    public String mainPage() {
        return "principal";
    }

    // ALTERADO: Redireciona a rota raiz ("/") para a nova tela principal
    @GetMapping("/")
    public String home() {
        return "redirect:/principal";
    }
}