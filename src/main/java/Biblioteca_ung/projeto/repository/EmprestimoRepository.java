package Biblioteca_ung.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Biblioteca_ung.projeto.model.Emprestimo;
import Biblioteca_ung.projeto.model.Usuario;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuario(Usuario usuario);
}
