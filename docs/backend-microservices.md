# Backend orientado a microservicos

O backend do ZeloDesk continua sendo um monolito Spring Boot, mas agora esta organizado por contexto de dominio para facilitar uma futura extracao em microservicos.

## Contextos atuais

- `com.zelodesk.identity`: usuarios, roles, autenticacao e consultas de identidade.
- `com.zelodesk.tickets`: abertura, consulta, metricas e ciclo de vida basico de tickets.
- `com.zelodesk.platform.security`: configuracao OAuth2/JWT e grant customizado.
- `com.zelodesk.shared`: excecoes e componentes transversais.

## Como isso ajuda na evolucao

- reduz acoplamento entre codigo de usuarios/autenticacao e codigo de tickets;
- deixa controladores, servicos e repositorios agrupados por responsabilidade de negocio;
- facilita extrair primeiro `identity` e `tickets` para servicos independentes sem reescrever o dominio;
- concentra componentes transversais em `shared`, evitando dependencias cruzadas desnecessarias.

## Possivel extracao futura

1. `identity-service`
2. `ticket-service`
3. `notification-service`
4. `audit-service`

## Regra de organizacao

Novas funcionalidades devem entrar primeiro no contexto de negocio correto. So componentes realmente compartilhados devem ir para `shared`.
