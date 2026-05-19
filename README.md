# ZeloDesk

Sistema de tickets de zeladoria para condominios — Laboratorio de Engenharia de Software, Grupo 10.

## Sobre o Projeto

O ZeloDesk e um sistema web para gestao de solicitacoes de zeladoria em condominios. Moradores e porteiros abrem tickets, a equipe de triagem prioriza e atribui executores, e os executores realizam o atendimento com registro de evidencia final.

### Casos de Uso do MVP

| ID | Caso de Uso | Ator | Descricao |
|---|---|---|---|
| UC-01 | Abrir ticket | Solicitante | Registra ocorrencia com categoria, localizacao, prioridade, anexo opcional e historico inicial |
| UC-02 | Triagem e atribuicao | Triagem | Define prioridade, atribui executor, pede complemento, cancela ou encerra o ticket |
| UC-03 | Execucao e conclusao | Executor | Assume atendimento, comenta e conclui ticket com evidencia final obrigatoria |

### Perfis de Usuario

- **Solicitante** — morador ou porteiro que abre tickets
- **Triagem** — responsavel por priorizar e atribuir tickets
- **Executor** — zelador que realiza o servico
- **Admin** — administrador do sistema

## Arquitetura

```
Frontend (Vue 3 + Vite)  →  Nginx (reverse proxy /api)  →  Backend (Spring Boot 3 + Java 21)
                                                              ↓
                                                         PostgreSQL 16
```

- **Frontend**: Vue 3 com Vite, servido por Nginx que faz proxy de `/api` para o backend
- **Backend**: Spring Boot 3.3 com Spring Security (OAuth2), Spring Data JPA, Lombok
- **Banco**: PostgreSQL 16
- **CI/CD**: Jenkins declarativo (JCasC + Job DSL) com Allure para relatorios de teste
- **Infra**: Terraform (Huawei Cloud) + Ansible para provisionamento automatizado

## Estrutura do Repositorio

```
.
├── ZeloDesk - BackEnd/zelodesk/   # Spring Boot (Java 21, Maven)
├── Zelodesk-frontend/             # Vue 3 + Vite
├── terraform/                     # Infra na Huawei Cloud (VPC, ECS, RDS, ELB, EIP)
├── ansible/                       # Playbooks de deploy (Docker, app, Jenkins)
├── jenkins/                       # Jenkins em codigo (Dockerfile, JCasC, Job DSL)
├── docs/                          # Documentacao, diagramas, wireframes
├── docker-compose.yml             # Compose local (postgres + backend + frontend)
├── docker-compose.cloud.yml       # Compose para ECS com RDS gerenciado
├── docker-compose.vm.yml          # Compose para VM com postgres local
├── .env.cloud.example             # Exemplo de env para deploy em nuvem
├── .env.vm.example                # Exemplo de env para deploy em VM
├── deploy.py                      # Script helper para deploy declarativo
├── Jenkinsfile                    # Pipeline CI (build, test, package)
└── .github/workflows/             # GitHub Actions
```

---

## Instalacao e Execucao Local

### Requisitos

- [Docker](https://docs.docker.com/get-docker/) e [Docker Compose](https://docs.docker.com/compose/install/)

### Com Docker Compose (recomendado)

```bash
docker compose up --build
```

Aguarde o build concluir. Os servicos ficam disponiveis em:

| Servico | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| PostgreSQL | localhost:5432 (banco `zelodesk`, usuario `zelodesk`, senha `zelodesk`) |

### Sem Docker (manual)

Requisitos adicionais: Java 21, Maven, Node.js 18+, npm, PostgreSQL 16

**Backend:**

```bash
cd "ZeloDesk - BackEnd/zelodesk"
APP_PROFILE=dev ./mvnw spring-boot:run
```

**Frontend:**

```bash
cd Zelodesk-frontend
npm install
VITE_API_URL=http://localhost:8080 npm run dev -- --host 0.0.0.0
```

### Usuarios de Teste

Criados automaticamente no perfil `dev`:

| Perfil | Email | Senha |
|---|---|---|
| Solicitante | `solicitante@zelodesk.com` | `123456` |
| Triagem | `triagem@zelodesk.com` | `123456` |
| Executor | `executor@zelodesk.com` | `123456` |
| Admin | `admin@zelodesk.com` | `123456` |

---

## Como Usar o ZeloDesk

### 1. Login

Acesse http://localhost:3000 e faca login com um dos usuarios de teste acima.

### 2. Abrir Ticket (Solicitante)

- Clique em **Novo ticket** no topo da pagina
- Preencha: titulo, descricao, categoria (Hidraulica, Eletrica, Limpeza, Manutencao, Seguranca), localizacao e prioridade (Alta, Media, Baixa)
- Anexe uma foto se necessario
- Clique em **Enviar**

### 3. Triagem (Triagem)

- Acesse a aba **Triagem** na barra lateral
- Selecione um ticket aberto
- Defina prioridade e atribua um executor
- Opcoes: pedir complemento ao solicitante, cancelar ou encerrar o ticket

### 4. Execucao (Executor)

- Acesse a aba **Meus tickets** para ver tickets atribuidos
- Clique em **Assumir** para iniciar o atendimento
- Adicione comentarios com atualizacoes de progresso
- Ao concluir, anexe a **evidencia final** (obrigatoria) e marque como **Concluido**

### 5. Dashboard e Kanban

- **Dashboard**: visao geral com metricas (tickets abertos, em execucao, concluidos)
- **Kanban**: board visual com colunas por status para arrastar e acompanhar tickets

---

## Deploy em Nuvem (Huawei Cloud)

O deploy e declarativo e dividido em tres camadas versionadas:

| Camada | Diretorio | Responsabilidade |
|---|---|---|
| Infra | `terraform/` | Cria VPC, subnet, ECS, RDS PostgreSQL, ELB, EIPs, Security Groups |
| Configuracao | `ansible/` | Instala Docker nas ECSs, sobe app e Jenkins |
| CI/CD | `jenkins/` | Jenkins em codigo (plugins, JCasC, Job DSL, Allure) |

### Requisitos para Deploy

- Python 3
- [Terraform](https://developer.hashicorp.com/terraform/downloads)
- [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
- `sshpass` (se usar senha SSH)
- Conta na Huawei Cloud com access key e secret key

### Deploy Completo

Preencha `terraform/terraform.tfvars` com suas credenciais e senhas, depois:

```bash
python3 deploy.py --apply-terraform
```

Para ambiente efemero com senha unica para tudo (ECS SSH, RDS, Jenkins, secrets):

```bash
python3 deploy.py --up --password 'SuaSenhaForte123!' --yes
```

Ou evite a senha no historico do shell:

```bash
ZELODESK_DEPLOY_PASSWORD='SuaSenhaForte123!' python3 deploy.py --up --yes
```

Se o Terraform ja foi aplicado, rode apenas:

```bash
python3 deploy.py
```

### Outros Comandos de Deploy

| Comando | Acao |
|---|---|
| `python3 deploy.py --status` | Checa URLs, login OAuth, SSH e containers remotos |
| `python3 deploy.py --down --password 'SENHA' --yes` | Destroi tudo e limpa arquivos locais |
| `python3 deploy.py --reset-terraform --password 'SENHA' --yes` | Apaga e recria infra do zero |
| `python3 deploy.py --configure-only` | Gera arquivos locais sem fazer deploy remoto |
| `python3 deploy.py --app-only` | Roda apenas o playbook da aplicacao |
| `python3 deploy.py --jenkins-only` | Roda apenas o playbook do Jenkins |

### URLs Apos Deploy

```bash
cd terraform
terraform output -raw public_url          # URL da aplicacao
terraform output -raw jenkins_url         # URL do Jenkins
terraform output -raw jenkins_webhook_url # URL do webhook GitHub
```

Configure o webhook do GitHub apontando para `terraform output -raw jenkins_webhook_url`.

---

## Deploy em VM (sem Terraform)

Para subir em uma VM que ja tem Docker instalado:

1. Copie o `.env.vm.example` para `.env` e ajuste as variaveis:

```bash
cp .env.vm.example .env
```

2. Edite `.env` com o IP publico da VM e senhas reais.

3. Suba os servicos:

```bash
docker compose -f docker-compose.vm.yml --env-file .env up -d --build
```

Para deploy com RDS gerenciado (sem container postgres), use `docker-compose.cloud.yml` com `.env.cloud.example`.

---

## Testes e Build

**Backend:**

```bash
cd "ZeloDesk - BackEnd/zelodesk"
./mvnw test          # testes unitarios
./mvnw package       # empacota o JAR
```

**Frontend:**

```bash
cd Zelodesk-frontend
npm run build        # build de producao
```

### CI/CD (Jenkins)

O pipeline definido no `Jenkinsfile` executa:

1. **Checkout** — clona o repositorio
2. **Build** — `mvn clean compile`
3. **Test** — `mvn test` com relatorios JUnit e Allure
4. **Package** — `mvn package -DskipTests` (apenas na branch `main`)

Acesso ao Jenkins: `terraform output -raw jenkins_url` (usuario: `admin`, senha definida no deploy)

---

## Documentacao

- `docs/casos-de-uso-principais-rascunho.md` — casos de uso detalhados (UC-01, UC-02, UC-03)
- `docs/diagramas/` — diagramas UML (casos de uso, classe, sequencia, arquitetura)
- `docs/wireframe/` — wireframes das telas
- `docs/KANBAN.md` — link do Trello do projeto
- `terraform/README.md` — documentacao do Terraform
- `ansible/README.md` — documentacao do Ansible
- `jenkins/README.md` — documentacao do Jenkins declarativo
- `ZeloDesk - BackEnd/zelodesk/TESTES.md` — documentacao dos testes do backend

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Frontend | Vue 3, Vite 5, Nginx |
| Backend | Spring Boot 3.3, Java 21, Spring Security (OAuth2), Spring Data JPA, Lombok |
| Banco | PostgreSQL 16 |
| Containerizacao | Docker, Docker Compose |
| IaC | Terraform (Huawei Cloud) |
| Configuracao | Ansible |
| CI/CD | Jenkins (JCasC, Job DSL), Allure |
| Versionamento | Git, GitHub |

## Grupo 10

Repositorio: [github.com/araujoviana/lab-eng-software-grupo10](https://github.com/araujoviana/lab-eng-software-grupo10)
