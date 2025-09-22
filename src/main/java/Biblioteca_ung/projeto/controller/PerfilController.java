package Biblioteca_ung.projeto.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

// NOVOS IMPORTS
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final PasswordEncoder passwordEncoder;

    // CONSTRUTOR: Injeta PasswordEncoder
    public PerfilController(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

        // 1. Validação de Confirmação de Senha
        if (!perfilCadastroDTO.getSenha().equals(perfilCadastroDTO.getNovaSenha())) {
            result.rejectValue("novaSenha", "error.perfilCadastroDTO", "As senhas não coincidem");
        }

        // 2. Validação de Email Duplicado
        if (usuarioRepository.existsByEmail(perfilCadastroDTO.getEmail())) {
            result.rejectValue("email", "error.perfilCadastroDTO", "Este e-mail já está em uso.");
        }

        // 3. Validação de CPF Duplicado
        if (usuarioRepository.existsByCpf(perfilCadastroDTO.getCpf())) {
            result.rejectValue("cpf", "error.perfilCadastroDTO", "Este CPF já está em uso.");
        }

        // 4. NOVA VALIDAÇÃO: CONFLITO ENTRE CARTEIRINHA E REGISTRO
        String nomeCompleto = perfilCadastroDTO.getNome().trim();
        // Verifica se há pelo menos um espaço (o que implica nome e sobrenome)
        if (!nomeCompleto.contains(" ") || nomeCompleto.split("\\s+").length < 2) {
            result.rejectValue("nome", "error.perfilCadastroDTO",
                    "Por favor, digite seu nome completo (nome e sobrenome).");
        }

        // 5. VALIDAÇÃO: UNICIDADE COMPLETA DOS CAMPOS DE IDENTIFICAÇÃO

        // 5a. Valida Carteirinha (se for usuário)
        if (perfilCadastroDTO.getTipo().equalsIgnoreCase("usuario") &&
                perfilCadastroDTO.getCarteirinha() != null &&
                !perfilCadastroDTO.getCarteirinha().isBlank()) {

            String numCarteirinha = perfilCadastroDTO.getCarteirinha();

            // Check 1: Conflito com outras Carteirinhas existentes (Unicidade interna)
            if (usuarioRepository.existsByCarteirinha(numCarteirinha)) {
                result.rejectValue("carteirinha", "error.perfilCadastroDTO",
                        "Este número de carteirinha já está em uso por outro usuário.");
            }
            // Check 2: Conflito com Registros de Bibliotecário (Unicidade cruzada)
            if (usuarioRepository.existsByRegistro(numCarteirinha)) {
                result.rejectValue("carteirinha", "error.perfilCadastroDTO",
                        "Este número de carteirinha é idêntico a um Registro de Bibliotecário já cadastrado.");
            }
        }

        // 5b. Valida Registro (se for bibliotecário)
        if (perfilCadastroDTO.getTipo().equalsIgnoreCase("bibliotecario") &&
                perfilCadastroDTO.getRegistro() != null &&
                !perfilCadastroDTO.getRegistro().isBlank()) {

            String numRegistro = perfilCadastroDTO.getRegistro();

            // Check 1: Conflito com outros Registros existentes (Unicidade interna)
            if (usuarioRepository.existsByRegistro(numRegistro)) {
                result.rejectValue("registro", "error.perfilCadastroDTO",
                        "Este número de registro já está em uso por outro bibliotecário.");
            }
            // Check 2: Conflito com Carteirinhas de Usuário (Unicidade cruzada)
            if (usuarioRepository.existsByCarteirinha(numRegistro)) {
                result.rejectValue("registro", "error.perfilCadastroDTO",
                        "Este número de registro é idêntico a uma Carteirinha de Usuário já cadastrada.");
            }
        }

        // Se for USUÁRIO: verifica se a carteirinha conflita com um Registro de
        // Bibliotecário
        if (perfilCadastroDTO.getTipo().equalsIgnoreCase("usuario") &&
                perfilCadastroDTO.getCarteirinha() != null &&
                !perfilCadastroDTO.getCarteirinha().isBlank()) {

            if (usuarioRepository.existsByRegistro(perfilCadastroDTO.getCarteirinha())) {
                result.rejectValue("carteirinha", "error.perfilCadastroDTO",
                        "Este número de carteirinha é idêntico a um Registro de Bibliotecário já cadastrado.");
            }
        }

        // Se for BIBLIOTECÁRIO: verifica se o registro conflita com uma Carteirinha de
        // Usuário
        if (perfilCadastroDTO.getTipo().equalsIgnoreCase("bibliotecario") &&
                perfilCadastroDTO.getRegistro() != null &&
                !perfilCadastroDTO.getRegistro().isBlank()) {

            if (usuarioRepository.existsByCarteirinha(perfilCadastroDTO.getRegistro())) {
                result.rejectValue("registro", "error.perfilCadastroDTO",
                        "Este número de registro é idêntico a uma Carteirinha de Usuário já cadastrada.");
            }
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
    public String salvarEdicaoPerfil(
            @ModelAttribute("usuario") Usuario usuarioAtualizado,
            @RequestParam(value = "novaSenha", required = false) String novaSenha,
            @RequestParam(value = "confirmaNovaSenha", required = false) String confirmaNovaSenha,
            Authentication authentication,
            RedirectAttributes ra) {

        Usuario usuarioOriginal = ((CustomUserDetails) authentication.getPrincipal()).getUsuario();

        // 1. Lógica de Alteração de Senha (Validação e Criptografia)
        if (novaSenha != null && !novaSenha.isBlank()) {
            if (!novaSenha.equals(confirmaNovaSenha)) {
                ra.addFlashAttribute("erro", "As novas senhas não coincidem.");
                return "redirect:/biblioteca/perfil/editar";
            }
            if (novaSenha.length() < 6) {
                ra.addFlashAttribute("erro", "A nova senha deve ter no mínimo 6 caracteres.");
                return "redirect:/biblioteca/perfil/editar";
            }

            // CORREÇÃO: Criptografa a nova senha e a define no objeto
            String novoHash = passwordEncoder.encode(novaSenha);
            usuarioOriginal.setSenha(novoHash);
        }
        // Se a senha não foi alterada, a senha hash ORIGINAL (do objeto
        // usuarioOriginal)
        // é mantida e será salva pelo service sem re-criptografia.

        // 2. Atualizar campos não-senha
        // Garante que o e-mail seja minúsculo para consistência no login
        usuarioOriginal.setEmail(usuarioAtualizado.getEmail().toLowerCase());

        // Atualiza os campos que o usuário pode editar no perfil
        usuarioOriginal.setTelefone(usuarioAtualizado.getTelefone());
        usuarioOriginal.setEndereco(usuarioAtualizado.getEndereco());
        usuarioOriginal.setUrlFoto(usuarioAtualizado.getUrlFoto());
        usuarioOriginal.setTipoUsuario(usuarioAtualizado.getTipoUsuario());
        usuarioOriginal.setCarteirinha(usuarioAtualizado.getCarteirinha());
        usuarioOriginal.setRegistro(usuarioAtualizado.getRegistro());

        try {
            // O UsuarioService (corrigido no passo anterior) salvará o objeto corretamente.
            usuarioService.salvar(usuarioOriginal);
            ra.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");
        } catch (DataIntegrityViolationException e) {
            // Captura falhas de unicidade/integridade (ex: Email/CPF duplicado) e
            // redireciona com erro
            logger.error("ERRO DE INTEGRIDADE DE DADOS ao atualizar perfil: {}", usuarioOriginal.getEmail(), e);
            ra.addFlashAttribute("erro",
                    "Erro: Este e-mail ou CPF já está em uso por outro usuário ou o formato é inválido.");
            return "redirect:/biblioteca/perfil/editar";
        } catch (Exception e) {
            // Captura qualquer outro erro inesperado e redireciona com erro
            logger.error("ERRO GENÉRICO ao salvar edição de perfil para {}", usuarioOriginal.getEmail(), e);
            ra.addFlashAttribute("erro", "Erro interno ao salvar perfil: " + e.getMessage());
            return "redirect:/biblioteca/perfil/editar";
        }

        return "redirect:/catalogo";
    }
}