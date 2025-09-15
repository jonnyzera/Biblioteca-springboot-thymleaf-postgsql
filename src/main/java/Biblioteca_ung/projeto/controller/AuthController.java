/*
 * package Biblioteca_ung.projeto.controller;
 * 
 * import org.springframework.stereotype.Controller;
 * import org.springframework.validation.BindingResult;
 * import org.springframework.web.bind.annotation.GetMapping;
 * import org.springframework.web.bind.annotation.ModelAttribute;
 * import org.springframework.web.bind.annotation.PostMapping;
 * 
 * import Biblioteca_ung.projeto.dto.UsuarioCadastroDTO;
 * import Biblioteca_ung.projeto.service.UsuarioService;
 * import ch.qos.logback.core.model.Model;
 * import jakarta.validation.Valid;
 * 
 * @Controller
 * public class AuthController {
 * 
 * private final UsuarioService usuarioService;
 * 
 * public AuthController(UsuarioService usuarioService) {
 * this.usuarioService = usuarioService;
 * }
 * 
 * @GetMapping("/cadastro")
 * public String cadastroForm(Model model) {
 * model.addAttribute("usuario", new UsuarioCadastroDTO("", "", "", "", ""));
 * return "cadastro"; // cadastro.html
 * }
 * 
 * @PostMapping("/cadastro")
 * public String cadastrar(@Valid @ModelAttribute("usuario") UsuarioCadastroDTO
 * dto,
 * BindingResult binding, Model model) {
 * if (binding.hasErrors()) {
 * return "cadastro";
 * }
 * try {
 * usuarioService.cadastrar(dto);
 * model.addAttribute("sucesso", "Cadastro realizado! Faça login.");
 * return "redirect:/login?sucesso=true";
 * } catch (IllegalArgumentException | IllegalStateException e) {
 * model.addAttribute("erro", e.getMessage());
 * return "cadastro";
 * }
 * }
 * 
 * 
 * @GetMapping("/login")
 * public String loginPage() {
 * return "login"; // login.html
 * }
 * 
 * @GetMapping("/biblioteca")
 * public String biblioteca() {
 * return "biblioteca"; // biblioteca.html (protegida)
 * }
 * }
 */