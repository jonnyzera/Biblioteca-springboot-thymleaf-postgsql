package Biblioteca_ung.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "cadastro-biblioteca";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}