package Biblioteca_ung.projeto.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;

}
