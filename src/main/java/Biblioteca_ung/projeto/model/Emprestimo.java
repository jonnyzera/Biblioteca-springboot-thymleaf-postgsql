package Biblioteca_ung.projeto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Livro livro;

    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    // Calcula os dias restantes para devolução
    public long getDiasRestantes() {
        if (dataDevolucao == null)
            return 0;
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), dataDevolucao);
        return dias < 0 ? 0 : dias;
    }
}
