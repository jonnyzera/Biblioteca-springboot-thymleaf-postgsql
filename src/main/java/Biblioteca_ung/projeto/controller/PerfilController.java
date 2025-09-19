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

// IMPORTS CRÍTICOS PARA LOG E TRATAMENTO DE ERRO
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
@RequestMapping("/biblioteca/perfil")
public class PerfilController {

    private static final Logger logger = LoggerFactory.getLogger(PerfilController.class);

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public PerfilController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/form")
    public String mostrarFormulario(Model model) {
        model.addAttribute("perfilCadastroDTO", new PerfilCadastroDTO());
        return "cadastro-biblioteca";
    }

    @PostMapping("/salvar")
    public String salvarPerfil(@ModelAttribute("perfilCadastroDTO") @Valid PerfilCadastroDTO perfilCadastroDTO,
            BindingResult result,
            Model model) {

        // --- VALIDAÇÕES DEFENSIVAS E DE UNICIDADE ---

        // 1. Validação manual para cobrir a falha do @Size na Senha (min 6 caracteres)
        if (perfilCadastroDTO.getSenha() != null && perfilCadastroDTO.getSenha().length() < 6) {
            result.rejectValue("senha", "error.perfilCadastroDTO", "A senha deve ter no mínimo 6 caracteres");
        }

        // 2. Validação de Confirmação de Senha
        if (!perfilCadastroDTO.getSenha().equals(perfilCadastroDTO.getNovaSenha())) {
            result.rejectValue("novaSenha", "error.perfilCadastroDTO", "As senhas não coincidem");
        }

        // 3. Validação de Email Duplicado
        if (usuarioRepository.existsByEmail(perfilCadastroDTO.getEmail())) {
            result.rejectValue("email", "error.perfilCadastroDTO", "Este e-mail já está em uso.");
        }

        // 4. Validação de CPF Duplicado
        if (usuarioRepository.existsByCpf(perfilCadastroDTO.getCpf())) {
            result.rejectValue("cpf", "error.perfilCadastroDTO", "Este CPF já está em uso.");
        }

        // Se houver erros de validação (incluindo o formato de CPF/Senha)
        if (result.hasErrors()) {
            model.addAttribute("perfilCadastroDTO", perfilCadastroDTO);
            return "cadastro-biblioteca";
        }

        // --- Mapeamento para o objeto Usuario ---
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(perfilCadastroDTO.getNome());
        novoUsuario.setEmail(perfilCadastroDTO.getEmail());
        novoUsuario.setSenha(perfilCadastroDTO.getSenha());
        novoUsuario.setCpf(perfilCadastroDTO.getCpf());

        // ESSENCIAL: Salva a Role correta ('USUARIO' ou 'BIBLIOTECARIO')s
        novoUsuario.setRole(perfilCadastroDTO.getTipo().toUpperCase());
        novoUsuario.setCarteirinha(perfilCadastroDTO.getCarteirinha());
        novoUsuario.setRegistro(perfilCadastroDTO.getRegistro());

        // --- CORREÇÃO CRÍTICA: TRY-CATCH para capturar a falha silenciosa do JPA ---
        try {
            usuarioService.salvar(novoUsuario);
            return "redirect:/login"; // SUCESSO
        } catch (DataIntegrityViolationException e) {
            // Captura falhas de unicidade/integridade do banco
            logger.error("ERRO DE INTEGRIDADE DE DADOS: CPF ou Email já cadastrado ou formato inválido.", e);
            result.rejectValue("cpf", "error.perfilCadastroDTO", "CPF ou E-mail já cadastrado, ou formato inválido.");
            model.addAttribute("perfilCadastroDTO", perfilCadastroDTO);
            return "cadastro-biblioteca"; // ERRO
        } catch (Exception e) {
            // Captura qualquer outro erro de persistência (como formato de campo que o JPA
            // não aceita)
            logger.error("ERRO CRÍTICO DE PERSISTÊNCIA: Falha ao salvar usuário no banco de dados.", e);
            model.addAttribute("salvarError",
                    "Erro interno: Falha ao completar o cadastro. Verifique se o CPF está no formato 000.000.000-00.");
            model.addAttribute("perfilCadastroDTO", perfilCadastroDTO);
            return "cadastro-biblioteca"; // ERRO
        }
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