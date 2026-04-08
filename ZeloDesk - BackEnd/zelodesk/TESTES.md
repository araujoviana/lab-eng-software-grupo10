# Testes JUnit - ZeloDesk

## Estrutura dos Testes

Os testes foram organizados em 5 arquivos — **33 testes no total**.

---

### 1. `AuthenticationTests.java` — Testes de Login
`src/test/java/com/zelodesk/services/AuthenticationTests.java`

| Teste | Descrição |
|-------|-----------|
| `testLoginUsuarioComum` | Login bem-sucedido com usuário comum |
| `testLoginAdmin` | Login bem-sucedido com admin |
| `testLoginEmailInexistente` | Login com email inexistente lança exceção |

---

### 2. `UsuarioControllerTests.java` — Testes dos Endpoints REST
`src/test/java/com/zelodesk/controllers/UsuarioControllerTests.java`

| Teste | Descrição |
|-------|-----------|
| `testFindByIdAsUsuario` | Usuário busca seu próprio ID |
| `testFindByIdAsAdmin` | Admin busca qualquer usuário por ID |
| `testFindAllAsAdmin` | Admin lista todos os usuários |
| `testFindAllAsUsuarioForbidden` | Usuário comum recebe 403 ao tentar listar todos |


### 3. `TicketServiceTests.java` — Testes do Serviço de Tickets
`src/test/java/com/zelodesk/services/TicketServiceTests.java`

| Teste | Descrição |
|-------|-----------|
| `testInsertTicket` | Cria ticket com sucesso e status inicial "Aberto" |
| `testInsertTicketGeraTicketCode` | TicketCode gerado no formato TKT-XXX |
| `testInsertTicketPrioridadeBaixa` | Cria ticket com prioridade BAIXA |
| `testUpdateStatusEmAndamento` | Muda status para "Em Andamento" |
| `testUpdateStatusConcluido` | Muda status para "Concluído" |
| `testUpdateStatusCancelado` | Muda status para "Cancelado" |
| `testUpdateStatusTicketInexistente` | Lança exceção para ticket inexistente |

---

### 4. `TicketControllerTests.java` — Testes dos Endpoints REST de Ticket
`src/test/java/com/zelodesk/controllers/TicketControllerTests.java`

| Teste | Descrição |
|-------|-----------|
| `testInsertTicketAsUsuario` | Usuário cria ticket (200 OK) |
| `testInsertTicketAsAdmin` | Admin cria ticket (200 OK) |
| `testInsertTicketSemToken` | Sem token retorna 401 |
| `testUpdateStatusEmAndamento` | PATCH muda status para "Em Andamento" |
| `testUpdateStatusConcluido` | PATCH muda status para "Concluído" |
| `testUpdateStatusCancelado` | PATCH muda status para "Cancelado" |
| `testUpdateStatusTicketInexistente` | PATCH com ID inválido retorna 404 |

---

### 5. `UsuarioRepositoryTests.java` — Testes da Camada de Dados
`src/test/java/com/zelodesk/repositories/UsuarioRepositoryTests.java`

| Teste | Descrição |
|-------|-----------|
| `testFindById` | Busca usuário por ID |
| `testFindByIdNotFound` | Retorna vazio para ID inexistente |
| `testSearchUserAndRolesByEmail` | Busca usuário e roles por email |
| `testSearchAdminAndRolesByEmail` | Busca admin e suas roles por email |
| `testSearchUserByEmailNotFound` | Email inexistente retorna lista vazia |
| `testFindAll` | Lista todos os usuários |
| `testFindCarlosSilvaById` | Encontra Carlos Silva pelo ID 3 |
| `testFindAnaSouzaById` | Encontra Ana Souza pelo ID 4 |
| `testSearchCarlosSilvaByEmail` | Busca Carlos Silva por email com role |
| `testSearchAnaSouzaByEmail` | Busca Ana Souza por email com role |
| `testUsuarioHasRole` | Usuário tem ROLE_USUARIO definida |
| `testAdminHasAdminRole` | Admin tem ROLE_ADMIN definida |

---

## Dados de Teste (`import.sql`)

| ID | Nome | Email | Role |
|----|------|-------|------|
| 1 | Admin Teste | admin@zelodesk.com | ROLE_ADMIN |
| 2 | Usuario Teste | usuario@zelodesk.com | ROLE_USUARIO |
| 3 | Carlos Silva | carlos@zelodesk.com | ROLE_USUARIO |
| 4 | Ana Souza | ana@zelodesk.com | ROLE_USUARIO |

**Senha para todos:** `123456`

---

## Como Executar

```bash
# Todos os testes
mvn test

# Classe específica
mvn test -Dtest=AuthenticationTests
mvn test -Dtest=UsuarioControllerTests
mvn test -Dtest=UsuarioRepositoryTests

# Teste específico
mvn test -Dtest=AuthenticationTests#testLoginAdmin
```
