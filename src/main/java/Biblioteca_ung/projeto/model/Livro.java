package Biblioteca_ung.projeto.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private Integer anoPublicacao;
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String urlCapa;

    private Integer quantidadeDisponivel;

    public boolean isDisponivel() {
        return this.quantidadeDisponivel > 0;
    }
}