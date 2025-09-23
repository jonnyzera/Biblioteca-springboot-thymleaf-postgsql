package Biblioteca_ung.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import Biblioteca_ung.projeto.model.Emprestimo;
import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.model.Usuario;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuario(Usuario usuario);

    // NOVO MÉTODO: Busca os livros mais emprestados
    @Query("SELECT e.livro FROM Emprestimo e GROUP BY e.livro ORDER BY COUNT(e.livro) DESC")
    List<Livro> findTopLivrosMaisEmprestados();
}
