package Biblioteca_ung.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaController {

    @GetMapping
    public String index() {
        return "redirect:/catalogo";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login-biblioteca";
    }
}