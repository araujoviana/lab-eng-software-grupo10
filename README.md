# Zelo Desk

Projeto de Laboratorio de Engenharia de Software - Grupo 10.

Sistema de tickets de zeladoria para condominios com frontend Vue.js, backend Spring Boot e PostgreSQL.

## Como rodar na VM

Requisito: Docker e Docker Compose instalados.

```bash
docker compose up --build
```

- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- PostgreSQL: `localhost:5432`, banco `zelodesk`, usuario `zelodesk`, senha `zelodesk`

Usuarios de teste criados automaticamente no perfil `dev`:

| Perfil | Email | Senha |
|---|---|---|
| Solicitante | `solicitante@zelodesk.com` | `123456` |
| Triagem | `triagem@zelodesk.com` | `123456` |
| Executor | `executor@zelodesk.com` | `123456` |
| Admin | `admin@zelodesk.com` | `123456` |

## Rodar manualmente

Backend:

```bash
cd "ZeloDesk - BackEnd/zelodesk"
APP_PROFILE=dev ./mvnw spring-boot:run
```

Frontend:

```bash
cd Zelodesk-frontend
npm install
VITE_API_URL=http://localhost:8080 npm run dev -- --host 0.0.0.0
```

## Testes e build

Backend:

```bash
cd "ZeloDesk - BackEnd/zelodesk"
./mvnw test
```

Frontend:

```bash
cd Zelodesk-frontend
npm run build
```

## Funcionalidades do MVP

- UC-01: abrir ticket com categoria, localizacao, prioridade, anexo opcional e historico inicial.
- UC-02: realizar triagem, definir prioridade, atribuir executor, pedir complemento, cancelar ou encerrar.
- UC-03: assumir atendimento, comentar e concluir ticket com evidencia final obrigatoria.
