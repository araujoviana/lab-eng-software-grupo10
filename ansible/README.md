# Ansible Deploy

Esta pasta tem playbooks para configurar as ECSs criadas pelo Terraform.

## Playbooks

- `playbook.yml`: configura a ECS da aplicação e sobe o ZeloDesk com `docker-compose.cloud.yml`.
- `jenkins.yml`: configura a ECS do Jenkins e sobe o Jenkins declarativo definido em `../jenkins`.

## Aplicação

O playbook da aplicação faz:

Ele faz:

- Instala Docker Engine e Docker Compose plugin.
- Instala Git.
- Clona ou atualiza o repositório em `/opt/zelodesk`.
- Lê `terraform output -raw application_env` localmente.
- Escreve `/opt/zelodesk/.env` na ECS.
- Executa `docker compose --env-file .env -f docker-compose.cloud.yml up -d --build`.

## Pré-Requisitos

- Terraform aplicado em `../terraform`.
- Ansible instalado localmente.
- `sshpass` instalado localmente se for usar senha com `--ask-pass`.
- EIP administrativo da ECS criado pelo Terraform.

## Uso

1. Aplique o Terraform:

```bash
cd ../terraform
terraform apply
```

2. Pegue os EIPs de SSH:

```bash
terraform output -raw ecs_ssh_eip
terraform output -raw jenkins_ssh_eip
```

3. Prepare o inventário:

```bash
cd ../ansible
cp inventory.ini.example inventory.ini
```

4. Edite `inventory.ini` e troque:

- `CHANGE_ME_ECS_SSH_EIP` pelo valor de `terraform output -raw ecs_ssh_eip`.
- `CHANGE_ME_JENKINS_SSH_EIP` pelo valor de `terraform output -raw jenkins_ssh_eip`.

5. Prepare as variáveis locais do Jenkins:

```bash
cp group_vars/jenkins.yml.example group_vars/jenkins.yml
```

Edite `group_vars/jenkins.yml` e configure `jenkins_admin_password`.

6. Rode os deploys usando a senha configurada em `ecs_admin_password`:

```bash
ansible-playbook -i inventory.ini playbook.yml --ask-pass
ansible-playbook -i inventory.ini jenkins.yml --ask-pass
```

Se a imagem não aceitar login como `root`, altere `ansible_user` no inventário para o usuário correto da imagem e rode com `--ask-become-pass` se necessário.

## Overrides Da Aplicação

Os defaults ficam no próprio `playbook.yml`. Se precisar mudar repo, branch ou diretório:

```bash
cp group_vars/zelodesk.yml.example group_vars/zelodesk.yml
```

Depois edite `group_vars/zelodesk.yml`.

`group_vars/zelodesk.yml` e `inventory.ini` são ignorados pelo Git para evitar vazar dados locais.

## Jenkins

O playbook `jenkins.yml` copia a pasta `../jenkins` para `/opt/zelodesk-jenkins` na ECS Jenkins e executa:

```bash
docker compose --env-file .env up -d --build
```

O Jenkins é configurado por código com:

- Plugins em `jenkins/plugins.txt`.
- JCasC em `jenkins/casc/jenkins.yaml`.
- Job DSL em `jenkins/jobs/zelodesk.groovy`.

Depois do deploy, configure o webhook do GitHub para:

```bash
cd ../terraform
terraform output -raw jenkins_webhook_url
```

## Outputs Usados

O playbook depende do output sensível abaixo:

```bash
terraform -chdir=../terraform output -raw application_env
```

Esse output contém as credenciais do RDS e o `CLIENT_SECRET`, então ele é escrito na ECS com permissão `0600`.
