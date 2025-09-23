package Biblioteca_ung.projeto.service;

import org.springframework.stereotype.Service;
import Biblioteca_ung.projeto.model.Emprestimo;
import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.EmprestimoRepository;
import Biblioteca_ung.projeto.repository.LivroRepository;
import Biblioteca_ung.projeto.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository,
            LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    public List<Emprestimo> listarPorUsuario(Usuario usuario) {
        return emprestimoRepository.findByUsuario(usuario);
    }

    // NOVO MÉTODO: Chama o método do repositório para listar os livros populares
    public List<Livro> listarLivrosPopulares(int limite) {
        return emprestimoRepository.findTopLivrosMaisEmprestados();
    }

    public Emprestimo salvar(Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    public void deletar(Long id) {
        emprestimoRepository.deleteById(id);
    }

    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id).orElse(null);
    }

    public void criarEmprestimo(Long usuarioId, Long livroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Livro não está disponível para empréstimo!");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(7));

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);
    }

    public void devolverEmprestimo(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado!"));

        Livro livro = emprestimo.getLivro();
        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livroRepository.save(livro);

        emprestimoRepository.delete(emprestimo);
    }
}