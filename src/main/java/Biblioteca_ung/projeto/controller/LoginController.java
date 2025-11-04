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

    // ALTERADO: Mapeamento para carregar o novo arquivo lading-page.html
    @GetMapping("/principal")
    public String mainPage() {
        return "lading-page";
    }

    // ALTERADO: Redireciona a rota raiz ("/") para a nova Landing Page
    @GetMapping("/")
    public String home() {
        return "lading-page"; // Retorna o template lading-page.html
    }
}