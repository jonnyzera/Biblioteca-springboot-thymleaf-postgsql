package Biblioteca_ung.projeto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "cpf") })
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;
    private String senha;
    private String role; // Campo adicionado

<<<<<<< HEAD
    // Getters e setters gerados pelo lombok @Data
=======
    @Column(nullable = false)
    private String senhaHash;

    @Column(unique = true, nullable = false, length = 14)
    private String cpf;
>>>>>>> 5da2da5a6b73c1f4d33fbe675d36431c6a2170dd
}
