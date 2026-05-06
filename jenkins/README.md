# Jenkins Declarativo

Esta pasta define o Jenkins em código:

- `Dockerfile`: imagem Jenkins LTS com JDK 21 e plugins instalados.
- `docker-compose.yml`: sobe Jenkins com volume persistente.
- `plugins.txt`: lista de plugins, incluindo GitHub, Pipeline, Job DSL, JCasC e Allure.
- `casc/jenkins.yaml`: Jenkins Configuration as Code.
- `jobs/zelodesk.groovy`: Job DSL que cria o pipeline `zelodesk-ci` apontando para o `Jenkinsfile` do repositório.

## Webhook GitHub

Depois do deploy, configure o webhook do GitHub para:

```text
http://JENKINS_EIP:8080/github-webhook/
```

O output do Terraform já mostra a URL exata:

```bash
cd ../terraform
terraform output -raw jenkins_webhook_url
```

## Acesso

O usuário e senha são definidos pelo Ansible no arquivo `/opt/zelodesk-jenkins/.env` da ECS Jenkins.

Por padrão:

- usuário: `admin`
- senha: definida em `ansible/group_vars/jenkins.yml`

## Allure

O Allure Jenkins Plugin e o Allure Commandline `2.29.1` são configurados via JCasC. O `Jenkinsfile` publica resultados de:

```text
ZeloDesk - BackEnd/zelodesk/target/allure-results
```
