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
        return "login-biblioteca"; // O arquivo .html que o log aponta como o correto é login-biblioteca
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}