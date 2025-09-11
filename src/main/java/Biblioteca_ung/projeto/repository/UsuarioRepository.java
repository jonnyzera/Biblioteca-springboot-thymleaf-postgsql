package Biblioteca_ung.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Biblioteca_ung.projeto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
