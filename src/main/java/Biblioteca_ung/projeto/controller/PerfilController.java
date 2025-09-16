package Biblioteca_ung.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import Biblioteca_ung.projeto.dto.PerfilCadastroDTO;
import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.service.UsuarioService;

@Controller
@RequestMapping("/biblioteca/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/form")
    public String mostrarFormulario(Model model) {
        model.addAttribute("perfilCadastroDTO", new PerfilCadastroDTO());
        return "cadastro-perfil";
    }

    @PostMapping("/salvar")
    public String salvarPerfil(@ModelAttribute("perfilCadastroDTO") @Valid PerfilCadastroDTO perfilCadastroDTO,
            BindingResult result,
            Model model) {

        if (!perfilCadastroDTO.getSenha().equals(perfilCadastroDTO.getNovaSenha())) {
            result.rejectValue("novaSenha", "error.perfilCadastroDTO", "As senhas não coincidem");
        }

        if (result.hasErrors()) {
            return "cadastro-perfil";
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(perfilCadastroDTO.getNome());
        novoUsuario.setEmail(perfilCadastroDTO.getEmail());
        novoUsuario.setSenha(perfilCadastroDTO.getSenha());
        novoUsuario.setRole(perfilCadastroDTO.getTipo().toUpperCase());
        usuarioService.salvar(novoUsuario);

        return "redirect:/biblioteca/cadastro/login";
    }
}