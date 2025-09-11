package Biblioteca_ung.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Biblioteca_ung.projeto.model.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

}
