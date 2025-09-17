package Biblioteca_ung.projeto.controller;

import org.springframework.security.core.Authentication; // Importação CORRETA
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
import Biblioteca_ung.projeto.repository.UsuarioRepository;

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
        novoUsuario.setCarteirinha(perfilCadastroDTO.getCarteirinha());
        novoUsuario.setRegistro(perfilCadastroDTO.getRegistro());

        usuarioService.salvar(novoUsuario);

        return "redirect:/login"; // Redireciona para a nova URL de login
    }

    @GetMapping("/editar")
    public String editarPerfil(Model model, Authentication authentication) {
        String userEmail = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(userEmail);
        model.addAttribute("usuario", usuario);
        model.addAttribute("activePage", "perfil"); // Adicionado para a navegação
        return "perfil";
    }

    @PostMapping("/salvar-edicao")
    public String salvarEdicaoPerfil(@ModelAttribute("usuario") Usuario usuarioAtualizado,
            Authentication authentication) {
        Usuario usuarioOriginal = usuarioService.buscarPorEmail(authentication.getName());

        usuarioOriginal.setEmail(usuarioAtualizado.getEmail());
        usuarioOriginal.setTelefone(usuarioAtualizado.getTelefone());
        usuarioOriginal.setEndereco(usuarioAtualizado.getEndereco());
        usuarioOriginal.setUrlFoto(usuarioAtualizado.getUrlFoto());
        usuarioOriginal.setTipoUsuario(usuarioAtualizado.getTipoUsuario());
        usuarioOriginal.setCarteirinha(usuarioAtualizado.getCarteirinha());
        usuarioOriginal.setRegistro(usuarioAtualizado.getRegistro());

        // Lógica de senha, se necessário, mas o formulário não tem
        usuarioService.salvar(usuarioOriginal);

        return "redirect:/catalogo";
    }
}