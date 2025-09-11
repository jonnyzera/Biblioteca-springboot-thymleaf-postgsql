package Biblioteca_ung.projeto.service;

import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {
    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public List<Livro> listarTodos() {
        return repository.findAll();
    }

    public Livro salvar(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio!");
        }
        return repository.save(livro);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Livro buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
