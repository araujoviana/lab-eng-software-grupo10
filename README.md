# Zelo Desk

Projeto de Laboratorio de Engenharia de Software - Grupo 10.

Sistema de tickets de zeladoria para condominios com frontend Vue.js, backend Spring Boot e PostgreSQL.

## Deploy Declarativo Na Huawei Cloud

O deploy de nuvem fica separado em tres camadas versionadas:

- `terraform/`: cria VPC, subnet, ECS da aplicacao, RDS PostgreSQL, ELB, EIPs e ECS do Jenkins.
- `ansible/`: instala Docker nas ECSs, sobe a aplicacao e sobe o Jenkins.
- `jenkins/`: define Jenkins em codigo com Docker Compose, plugins, JCasC, Job DSL, GitHub webhook e Allure.

Fluxo mais simples, depois de preencher `terraform/terraform.tfvars`:

```bash
python3 deploy.py --apply-terraform
```

Para ambiente efemero de teste usando uma senha unica para ECS SSH, RDS, Jenkins e secrets gerados:

```bash
python3 deploy.py --up --password 'CHANGE_ME_Strong123!' --yes
```

Tambem da para evitar a senha no historico do shell:

```bash
ZELODESK_DEPLOY_PASSWORD='CHANGE_ME_Strong123!' python3 deploy.py --up --yes
```

Se o Terraform ja foi aplicado, rode apenas:

```bash
python3 deploy.py
```

Para destruir tudo e limpar os arquivos locais gerados pelo deploy:

```bash
python3 deploy.py --down --password 'CHANGE_ME_Strong123!' --yes
```

Para checar URLs, login OAuth, SSH e containers remotos:

```bash
ZELODESK_DEPLOY_PASSWORD='CHANGE_ME_Strong123!' python3 deploy.py --status
```

Para apagar e recriar a infraestrutura do zero:

```bash
python3 deploy.py --reset-terraform --password 'CHANGE_ME_Strong123!' --yes
```

Essa opcao roda `terraform destroy` e depois `terraform apply`, entao apaga VMs, RDS/banco e demais recursos pagos antes de recriar tudo.

O script le os outputs do Terraform, gera `ansible/inventory.ini`, gera `ansible/group_vars/jenkins.yml` e pergunta antes de rodar os playbooks, exceto quando `--yes` for usado.

Quando `--password` nao for usado, o Ansible vai pedir a senha SSH da ECS configurada em `ecs_admin_password` no Terraform.

Para so gerar os arquivos locais sem fazer deploy remoto:

```bash
python3 deploy.py --configure-only
```

Para rodar apenas uma parte:

```bash
python3 deploy.py --app-only
python3 deploy.py --jenkins-only
```

URLs uteis:

```bash
cd terraform
terraform output -raw public_url
terraform output -raw jenkins_url
terraform output -raw jenkins_webhook_url
```

Configure o webhook do GitHub apontando para `terraform output -raw jenkins_webhook_url`.

Guias detalhados:

- `terraform/README.md`
- `ansible/README.md`
- `jenkins/README.md`

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
