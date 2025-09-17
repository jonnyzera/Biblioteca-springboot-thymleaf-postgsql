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
    private String descricao;
    private String urlCapa;

    // A lógica de disponibilidade agora é baseada na quantidade
    private Integer quantidadeDisponivel;

    public boolean isDisponivel() {
        return this.quantidadeDisponivel > 0;
    }
}
