package Biblioteca_ung.projeto.service;

import Biblioteca_ung.projeto.model.Emprestimo;
import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.EmprestimoRepository;
import Biblioteca_ung.projeto.repository.LivroRepository;
import Biblioteca_ung.projeto.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private EmprestimoService emprestimoService;

    private Usuario usuario;
    private Livro livro;
    private Emprestimo emprestimo;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        livro = new Livro();
        livro.setId(10L);
        livro.setTitulo("O Iluminado");
        livro.setQuantidadeDisponivel(3);

        emprestimo = new Emprestimo();
        emprestimo.setId(50L);
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
    }

    @Test
    void criarEmprestimo_comSucesso_diminuiQuantidadeLivro() {
        // Arrange: Simula que o usuário e o livro foram encontrados
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(10L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.save(any(Emprestimo.class))).thenReturn(emprestimo);
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        // Act
        emprestimoService.criarEmprestimo(1L, 10L);

        // Assert
        assertEquals(2, livro.getQuantidadeDisponivel(), "A quantidade de livros deve ser decrementada (3 -> 2)");
        verify(livroRepository, times(1)).save(livro);
        verify(emprestimoRepository, times(1)).save(any(Emprestimo.class));
    }

    @Test
    void criarEmprestimo_livroIndisponivel_lancaExcecao() {
        // Arrange
        livro.setQuantidadeDisponivel(0); // Livro indisponível
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(10L)).thenReturn(Optional.of(livro));

        // Act & Assert: Verifica se a exceção é lançada antes de qualquer salvamento
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            emprestimoService.criarEmprestimo(1L, 10L);
        });

        assertEquals("Livro não está disponível para empréstimo!", thrown.getMessage());
        verify(livroRepository, never()).save(any(Livro.class));
        verify(emprestimoRepository, never()).save(any(Emprestimo.class));
    }

    @Test
    void devolverEmprestimo_comSucesso_aumentaQuantidadeLivro() {
        // Arrange
        livro.setQuantidadeDisponivel(2); // Simula que 1 livro foi emprestado (Originalmente 3)
        when(emprestimoRepository.findById(50L)).thenReturn(Optional.of(emprestimo));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        doNothing().when(emprestimoRepository).delete(emprestimo);

        // Act
        emprestimoService.devolverEmprestimo(50L);

        // Assert
        assertEquals(3, livro.getQuantidadeDisponivel(), "A quantidade de livros deve ser incrementada (2 -> 3)");
        verify(livroRepository, times(1)).save(livro);
        verify(emprestimoRepository, times(1)).delete(emprestimo);
    }

    @Test
    void devolverEmprestimo_emprestimoNaoEncontrado_lancaExcecao() {
        // Arrange
        when(emprestimoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            emprestimoService.devolverEmprestimo(999L);
        });

        assertEquals("Empréstimo não encontrado!", thrown.getMessage());
        verify(livroRepository, never()).save(any(Livro.class));
    }
}