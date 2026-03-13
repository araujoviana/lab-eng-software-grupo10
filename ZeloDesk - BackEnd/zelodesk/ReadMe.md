# Dependências do Projeto ZeloDesk

## Dependências Principais

**Spring Boot Starter Web MVC**
Responsável por criar a camada web da aplicação, permitindo a criação de endpoints REST e controllers MVC.

**Spring Boot Starter Data JPA**
Integração com banco de dados via JPA/Hibernate. Permite o uso de repositórios para operações de leitura, escrita, atualização e remoção de dados.

**Spring Boot Starter Security**
Adiciona autenticação e autorização à aplicação, protegendo os endpoints e gerenciando o acesso dos usuários.

**Spring Boot Starter Validation**
Habilita a validação de dados nas requisições, permitindo o uso de anotações como `@NotNull`, `@NotBlank`, `@Size`, entre outras.

**Spring Boot H2 Console**
Disponibiliza uma interface web para visualização e manipulação do banco de dados H2 diretamente pelo navegador.

---

## Banco de Dados

**H2 Database**
Banco de dados em memória utilizado durante o desenvolvimento. Os dados são reiniciados a cada execução da aplicação.
