# Testes JUnit - ZeloDesk

## Estrutura dos Testes

Os testes foram organizados em 6 arquivos — **36 testes no total**.

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
| `deveAbrirTicketComHistoricoInicial` | UC-01 cria ticket com status inicial e histórico |
| `deveListarTicketsComFiltroDeStatus` | RF07 lista tickets com filtro de status |
| `deveRealizarTriagemEAtribuirExecutor` | UC-02 define prioridade e executor |
| `deveExigirResponsavelNaTriagem` | Valida regra de atribuição de responsável |
| `deveConcluirTicketComComentarioEEvidencia` | UC-03 conclui ticket com comentário e evidência |
| `deveBloquearConclusaoSemEvidencia` | Bloqueia conclusão sem evidência final |
| `deveFalharParaTicketInexistente` | Lança exceção para ticket inexistente |

---

### 4. `TicketControllerTests.java` — Testes dos Endpoints REST de Ticket
`src/test/java/com/zelodesk/controllers/TicketControllerTests.java`

| Teste | Descrição |
|-------|-----------|
| `deveCriarTicketComoSolicitante` | UC-01 cria ticket autenticado |
| `deveExigirAutenticacao` | Endpoints de ticket exigem token |
| `deveListarTickets` | RF07 lista tickets autenticado |
| `deveRealizarTriagem` | UC-02 registra triagem via API |
| `deveBloquearTriagemParaSolicitante` | Solicitante não acessa triagem |
| `deveConcluirTicket` | UC-03 conclui ticket via API |
| `deveBloquearConclusaoSemEvidencia` | API rejeita conclusão sem evidência |
| `deveRetornar404ParaTicketInexistente` | Busca inexistente retorna 404 |

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
| 5 | Maria Oliveira | solicitante@zelodesk.com | ROLE_SOLICITANTE |
| 6 | Carla Mendes | triagem@zelodesk.com | ROLE_TRIAGEM |
| 7 | Joao Pereira | executor@zelodesk.com | ROLE_EXECUTOR |

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
