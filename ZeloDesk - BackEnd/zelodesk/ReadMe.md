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
Disponibiliza uma interface web para visualização do banco H2 usado nos testes automatizados.

---

## Banco de Dados

**PostgreSQL**
Banco principal do projeto no perfil `dev`, configurado por `DB_URL`, `DB_USER` e `DB_PASSWORD`.

**H2 Database**
Banco em memória utilizado no perfil `test` para execução rápida dos testes automatizados.

---

## Utilitários

**Lombok**
Biblioteca que reduz significativamente a quantidade de código boilerplate em classes Java. Utiliza anotações em tempo de compilação para gerar automaticamente métodos repetitivos como getters, setters, construtores, métodos `toString()`, `equals()` e `hashCode()`.

**Principais anotações do Lombok:**

- `@Getter` - Gera automaticamente os métodos getter para todos os atributos
- `@Setter` - Gera automaticamente os métodos setter para todos os atributos
- `@Data` - Combina `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode` e `@RequiredArgsConstructor`
- `@NoArgsConstructor` - Gera um construtor sem argumentos
- `@AllArgsConstructor` - Gera um construtor com argumentos para todos os atributos
- `@RequiredArgsConstructor` - Gera um construtor para atributos marcados como `final`
- `@ToString` - Gera automaticamente o método `toString()`
- `@EqualsAndHashCode` - Gera automaticamente os métodos `equals()` e `hashCode()`
- `@Builder` - Implementa o padrão Builder para construção de objetos de forma mais legível

**Benefícios:**
- Reduz significativamente linhas de código
- Melhora a legibilidade das classes
- Facilita manutenção ao evitar código duplicado
- Gera automaticamente métodos padrão da linguagem Java

---

## Dependências de Teste

**Spring Boot Starter Data JPA Test**
Ferramentas para testar a camada de acesso a dados. Permite testar repositories e validar queries do JPA em ambientes isolados.

**Spring Boot Starter Security Test**
Oferece utilitários para testar autenticação e autorização. Permite simular usuários autenticados e testar endpoints protegidos.
