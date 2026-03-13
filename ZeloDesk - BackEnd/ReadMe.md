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

**Spring Boot Starter Validation Test**
Ferramentas para testar as regras de validação da aplicação, garantindo que as anotações de validação funcionem corretamente.

**Spring Boot Starter WebMVC Test**
Fornece utilitários para testar controllers e requisições HTTP. Utiliza MockMvc para simular requisições sem necessidade de iniciar o servidor.
