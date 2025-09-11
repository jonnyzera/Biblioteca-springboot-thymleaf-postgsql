package Biblioteca_ung.projeto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import Biblioteca_ung.projeto.model.Emprestimo;
import Biblioteca_ung.projeto.model.Livro;
import Biblioteca_ung.projeto.model.Usuario;
import Biblioteca_ung.projeto.repository.EmprestimoRepository;
import Biblioteca_ung.projeto.repository.LivroRepository;
import Biblioteca_ung.projeto.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.Scanner;

@Profile("terminal")
@Component
public class BibliotecaConsole implements CommandLineRunner {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;

    public BibliotecaConsole(LivroRepository livroRepository,
            UsuarioRepository usuarioRepository,
            EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===== SISTEMA BIBLIOTECA UNG CENTRO =====");
            System.out.println("1 - Cadastrar Livro");
            System.out.println("2 - Listar Livros");
            System.out.println("3 - Cadastrar Usuário");
            System.out.println("4 - Listar Usuários");
            System.out.println("5 - Criar Empréstimo");
            System.out.println("6 - Listar Empréstimos");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> {
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();
                    System.out.print("Ano: ");
                    int ano = scanner.nextInt();
                    Livro livro = new Livro();
                    livro.setTitulo(titulo);
                    livro.setAutor(autor);
                    livro.setAnoPublicacao(ano);
                    livroRepository.save(livro);
                    System.out.println("Livro salvo!");
                }
                case 2 -> livroRepository.findAll()
                        .forEach(l -> System.out.println(l.getId() + " - " + l.getTitulo()));
                case 3 -> {
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuarioRepository.save(usuario);
                    System.out.println("Usuário salvo!");
                }
                case 4 -> usuarioRepository.findAll()
                        .forEach(u -> System.out.println(u.getId() + " - " + u.getNome()));
                case 5 -> {
                    System.out.print("ID do Livro: ");
                    Long idLivro = scanner.nextLong();
                    System.out.print("ID do Usuário: ");
                    Long idUsuario = scanner.nextLong();
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setLivro(livroRepository.findById(idLivro).orElseThrow());
                    emprestimo.setUsuario(usuarioRepository.findById(idUsuario).orElseThrow());
                    emprestimo.setDataEmprestimo(LocalDate.now());
                    emprestimoRepository.save(emprestimo);
                    System.out.println("Empréstimo salvo!");
                }
                case 6 -> emprestimoRepository.findAll()
                        .forEach(e -> System.out.println(e.getId() + " - Livro: " + e.getLivro().getTitulo() +
                                " | Usuário: " + e.getUsuario().getNome() +
                                " | Data: " + e.getDataEmprestimo()));
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
