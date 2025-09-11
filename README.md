# 📚 Sistema de Biblioteca - Spring Boot + PostgreSQL + Thymeleaf

Este projeto é um sistema simples de gerenciamento de biblioteca desenvolvido com **Java**, **Spring Boot**, **Thymeleaf** e **PostgreSQL**. Ele permite cadastrar livros, usuários e controlar empréstimos de forma prática e intuitiva.

## 🚀 Funcionalidades

- Cadastro de livros com título, autor e status de disponibilidade
- Cadastro de usuários com nome e e-mail
- Registro de empréstimos com data de devolução e cálculo de dias restantes
- Interface web com Thymeleaf
- Persistência de dados com PostgreSQL
- Validações básicas via camada de serviço

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Bootstrap 5 (via CDN)
- Maven

## 📦 Estrutura do Projeto

Biblioteca_ung.projeto/ ├── controller/ │ ├── LivroController.java │ ├── UsuarioController.java │ └── EmprestimoController.java ├── model/ │ ├── Livro.java │ ├── Usuario.java │ └── Emprestimo.java ├── repository/ │ ├── LivroRepository.java │ ├── UsuarioRepository.java │ └── EmprestimoRepository.java ├── service/ │ ├── LivroService.java │ ├── UsuarioService.java │ └── EmprestimoService.java └── resources/ ├── templates/ │ └── biblioteca.html └── application.properties

## ⚙️ Configuração do Banco de Dados

Certifique-se de ter o PostgreSQL instalado e crie o banco:

```sql
CREATE DATABASE biblioteca;
```

## No arquivo application.properties:

```spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## ▶️ Como Executar
Clone o repositório

Configure o banco no application.properties

Execute com Maven ou sua IDE favorita:
```
mvn spring-boot:run
```
Acesse no navegador:
```
http://localhost:8080/biblioteca
```

## 👨‍💻 Autores (8º Semestre do Curso Ciência da Computação - Tizinho ) 
**Disciplina: Fabrica de Software - Professor: Anselmo Universidade Guarulhos - UNG CENTRO**
- João Victor Engenheiro de Java e DevOps | Estudante de Ciência da Computação - Contato: [joaovictor8600@yahoo.com.br]
- Leonardo Porcel Analista de dados Python | Estudante de Ciência da Computação
- Guilherme Silveira Ciêntista em dados Python |  Estudante de Ciência da Computação
- Leandro Aquino Design UX|IX e front-end Angular| Estudante de Ciência da Computação

