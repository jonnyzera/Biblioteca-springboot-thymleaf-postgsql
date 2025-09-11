package Biblioteca_ung.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Biblioteca_ung.projeto.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}
