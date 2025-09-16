package Biblioteca_ung.projeto.service;

import Biblioteca_ung.projeto.dto.UsuarioCadastroDTO;
import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.UsuarioRepository;
<<<<<<< HEAD

import org.springframework.security.crypto.password.PasswordEncoder;
=======
import jakarta.transaction.Transactional;

>>>>>>> 5da2da5a6b73c1f4d33fbe675d36431c6a2170dd
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

<<<<<<< HEAD
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncode) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncode;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

=======
    /*
     * private final PasswordEncoder passwordEncoder;
     * 
     * public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder
     * passwordEncoder) {
     * this.usuarioRepository = usuarioRepository;
     * this.passwordEncoder = passwordEncoder;
     * }
     * 
     * @Transactional
     * public Usuario cadastrar(UsuarioCadastroDTO dto) {
     * if (!dto.senha().equals(dto.confirmaSenha())) {
     * throw new IllegalArgumentException("As senhas não conferem.");
     * }
     * if (usuarioRepository.existsByEmail(dto.email())) {
     * throw new IllegalStateException("E-mail já cadastrado.");
     * }
     * if (usuarioRepository.existsByCpf(dto.cpf())) {
     * throw new IllegalStateException("CPF já cadastrado.");
     * }
     * 
     * Usuario u = new Usuario();
     * u.setNome(dto.nome());
     * u.setEmail(dto.email().toLowerCase().trim());
     * u.setCpf(dto.cpf());
     * u.setSenhaHash(passwordEncoder.encode(dto.senha()));
     * return usuarioRepository.save(u);
     * }
     * 
     * public Usuario buscarPorEmail(String email) {
     * return usuarioRepository.findByEmail(email.toLowerCase().trim())
     * .orElse(null);
     * }
     * 
     * // Validação Usuário
     * public List<Usuario> listarTodos() {
     * return usuarioRepository.findAll();
     * }
     * 
     * public Usuario salvar(Usuario usuario) {
     * if (usuario.getNome() == null || usuario.getNome().isBlank()) {
     * throw new IllegalArgumentException("Nome não pode ser vazio!");
     * }
     * return usuarioRepository.save(usuario);
     * }
     */
>>>>>>> 5da2da5a6b73c1f4d33fbe675d36431c6a2170dd
    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}