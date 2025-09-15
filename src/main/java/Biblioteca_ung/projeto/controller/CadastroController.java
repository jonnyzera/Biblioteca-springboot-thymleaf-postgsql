/*
 * package Biblioteca_ung.projeto.controller;
 * 
 * import org.springframework.stereotype.Controller;
 * import org.springframework.web.bind.annotation.GetMapping;
 * import org.springframework.web.bind.annotation.ModelAttribute;
 * import org.springframework.web.bind.annotation.PostMapping;
 * import org.springframework.web.bind.annotation.RequestMapping;
 * 
 * import Biblioteca_ung.projeto.model.Usuario;
 * import Biblioteca_ung.projeto.service.UsuarioService;
 * 
 * @Controller
 * 
 * @RequestMapping("/biblioteca/cadastro")
 * public class CadastroController {
 * private final UsuarioService usuarioService;
 * 
 * public CadastroController(UsuarioService usuarioService) {
 * this.usuarioService = usuarioService;
 * }
 * 
 * @GetMapping
 * public String mostrarFormulario() {
 * return "cadastro-biblioteca";
 * }
 * 
 * @PostMapping("/salvar")
 * public String salvarCadastro(@ModelAttribute Usuario usuario) {
 * usuarioService.salvar(usuario);
 * return "redirect:/biblioteca";
 * }
 * }
 */
