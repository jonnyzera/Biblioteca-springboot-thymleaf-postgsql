package Biblioteca_ung.projeto.controller;

import org.springframework.security.core.Authentication;
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
import Biblioteca_ung.projeto.service.CustomUserDetails;
import Biblioteca_ung.projeto.service.UsuarioService;
import Biblioteca_ung.projeto.repository.UsuarioRepository;

@Controller
@RequestMapping("/biblioteca/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

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
        novoUsuario.setCarteirinha(perfilCadastroDTO.getCarteirinha());
        novoUsuario.setRegistro(perfilCadastroDTO.getRegistro());

        usuarioService.salvar(novoUsuario);

        return "redirect:/login";
    }

    @GetMapping("/editar")
    public String editarPerfil(Model model, Authentication authentication) {
        // Acessa o objeto Usuario diretamente do principal
        Usuario usuario = ((CustomUserDetails) authentication.getPrincipal()).getUsuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("activePage", "perfil");
        return "perfil";
    }

    @PostMapping("/salvar-edicao")
    public String salvarEdicaoPerfil(@ModelAttribute("usuario") Usuario usuarioAtualizado,
            Authentication authentication) {
        // Acessa o objeto Usuario diretamente do principal
        Usuario usuarioOriginal = ((CustomUserDetails) authentication.getPrincipal()).getUsuario();

        usuarioOriginal.setEmail(usuarioAtualizado.getEmail());
        usuarioOriginal.setTelefone(usuarioAtualizado.getTelefone());
        usuarioOriginal.setEndereco(usuarioAtualizado.getEndereco());
        usuarioOriginal.setUrlFoto(usuarioAtualizado.getUrlFoto());
        usuarioOriginal.setTipoUsuario(usuarioAtualizado.getTipoUsuario());
        usuarioOriginal.setCarteirinha(usuarioAtualizado.getCarteirinha());
        usuarioOriginal.setRegistro(usuarioAtualizado.getRegistro());

        usuarioService.salvar(usuarioOriginal);

        return "redirect:/catalogo";
    }
}