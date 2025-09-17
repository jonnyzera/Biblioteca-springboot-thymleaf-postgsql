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
import Biblioteca_ung.projeto.repository.UsuarioRepository; // Adicione esta importação

@Controller
@RequestMapping("/biblioteca/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository; // Adicione o repositório

    public PerfilController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
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

        if (usuarioRepository.existsByEmail(perfilCadastroDTO.getEmail())) {
            result.rejectValue("email", "error.perfilCadastroDTO", "Este e-mail já está em uso.");
        }

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
        novoUsuario.setCpf(perfilCadastroDTO.getCpf());
        novoUsuario.setRole(perfilCadastroDTO.getTipo().toUpperCase());
        usuarioService.salvar(novoUsuario);

        return "redirect:/login"; // Redireciona para a nova URL de login
    }
}