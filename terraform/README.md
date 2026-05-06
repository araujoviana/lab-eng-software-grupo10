# Terraform HuaweiCloud Deploy

Infraestrutura para rodar o ZeloDesk na Huawei Cloud, região Singapura (`ap-southeast-3`):

- VPC e subnet privada.
- ECS Ubuntu simples para Docker Compose.
- RDS PostgreSQL gerenciado.
- Elastic Load Balancer público.
- Elastic IP associado ao ELB.
- Elastic IP administrativo associado diretamente à ECS para SSH/Ansible.
- ECS Jenkins separada com EIP próprio para CI e webhook GitHub.
- Security Groups separando app e banco.

## Uso

1. Crie um arquivo local de variáveis:

```bash
cp terraform.tfvars.example terraform.tfvars
```

2. Edite `terraform.tfvars` com senhas reais e, se necessário, ajuste `availability_zone`, `ecs_flavor_id`, `ecs_image_name` e `rds_flavor` conforme disponibilidade da sua conta em Singapura.

3. Exporte as credenciais ou coloque `access_key` e `secret_key` no `terraform.tfvars` local:

```bash
export HW_ACCESS_KEY="..."
export HW_SECRET_KEY="..."
export HW_REGION_NAME="ap-southeast-3"
```

4. Inicialize e aplique:

```bash
terraform init
terraform plan
terraform apply
```

5. Depois do apply, gere o `.env` sugerido para usar na ECS:

```bash
terraform output -raw application_env
```

Outputs principais:

```bash
terraform output -raw public_url
terraform output -raw ecs_ssh_eip
terraform output -raw jenkins_url
terraform output -raw jenkins_ssh_eip
terraform output -raw jenkins_webhook_url
```

## Deploy Da Aplicação Na ECS

O Terraform cria a VM com senha (`ecs_admin_password`) e um EIP administrativo para SSH/Ansible, mas não instala Docker automaticamente. Isso evita o conflito da HuaweiCloud em que `user_data` via cloud-init pode invalidar o `admin_pass` em imagens Linux.

O caminho recomendado é usar o playbook em `../ansible`:

```bash
cd ../ansible
cp inventory.ini.example inventory.ini
```

Edite `inventory.ini` usando o IP retornado por:

```bash
cd ../terraform
terraform output -raw ecs_ssh_eip
```

Depois rode:

```bash
cd ../ansible
ansible-playbook -i inventory.ini playbook.yml --ask-pass
ansible-playbook -i inventory.ini jenkins.yml --ask-pass
```

O ELB encaminha tráfego HTTP público para a porta `80` da ECS. O compose do projeto publica o frontend nessa porta, e o Nginx do frontend encaminha `/api/` internamente para o backend.

## Jenkins

O Jenkins roda em uma ECS separada para não concorrer com a aplicação durante builds e testes.

O Terraform cria:

- `jenkins_url`: URL pública do Jenkins.
- `jenkins_webhook_url`: URL para configurar no webhook do GitHub.
- `jenkins_ssh_eip`: EIP administrativo usado pelo Ansible.

O Jenkins em si é configurado pelo Ansible usando os arquivos versionados em `../jenkins`.

## Observações

- `application_db_name` vem como `postgres` porque o recurso RDS da HuaweiCloud cria a instância, mas não cria um database `zelodesk` separado. Se quiser usar `DB_NAME=zelodesk`, crie esse database no RDS e altere a variável.
- `JPA_DDL_AUTO=update` é adequado para demo/projeto de faculdade, mas em produção real seria melhor usar migrations.
- O Security Group do RDS aceita `5432` apenas a partir do Security Group da ECS.
- A aplicação pública continua entrando pelo EIP do ELB; o EIP da ECS é apenas administrativo para SSH/Ansible.
- O Jenkins usa porta `8080`; restrinja `jenkins_allowed_cidr` no `terraform.tfvars` se não quiser deixá-lo público.
- `terraform.tfvars`, estados e arquivos `.terraform/` são ignorados para evitar commit de segredos.
