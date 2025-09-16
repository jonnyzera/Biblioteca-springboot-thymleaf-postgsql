package Biblioteca_ung.projeto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Biblioteca_ung.projeto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
