output "public_url" {
  description = "Public URL served by the ELB EIP."
  value       = "http://${huaweicloud_vpc_eip.elb.address}"
}

output "elb_eip" {
  description = "Elastic IP attached to the public ELB."
  value       = huaweicloud_vpc_eip.elb.address
}

output "ecs_private_ip" {
  description = "Private IP of the ECS that receives ELB traffic on port 80."
  value       = huaweicloud_compute_instance.app.network[0].fixed_ip_v4
}

output "ecs_ssh_eip" {
  description = "Administrative Elastic IP attached directly to the ECS for SSH/Ansible."
  value       = huaweicloud_vpc_eip.ecs_ssh.address
}

output "ssh_command" {
  description = "SSH command for the ECS using the default HuaweiCloud Linux admin user."
  value       = "ssh root@${huaweicloud_vpc_eip.ecs_ssh.address}"
}

output "jenkins_url" {
  description = "Public Jenkins URL. Use /github-webhook/ for GitHub push hooks."
  value       = "http://${huaweicloud_vpc_eip.jenkins.address}:8080"
}

output "jenkins_webhook_url" {
  description = "GitHub webhook URL for Jenkins GitHub plugin."
  value       = "http://${huaweicloud_vpc_eip.jenkins.address}:8080/github-webhook/"
}

output "jenkins_ssh_eip" {
  description = "Administrative Elastic IP attached directly to the Jenkins ECS for SSH/Ansible."
  value       = huaweicloud_vpc_eip.jenkins.address
}

output "jenkins_ssh_command" {
  description = "SSH command for the Jenkins ECS using the default HuaweiCloud Linux admin user."
  value       = "ssh root@${huaweicloud_vpc_eip.jenkins.address}"
}

output "rds_private_ips" {
  description = "Private IPs of the RDS PostgreSQL instance."
  value       = huaweicloud_rds_instance.postgres.private_ips
}

output "rds_port" {
  description = "PostgreSQL port exposed inside the VPC."
  value       = 5432
}

output "application_env" {
  description = "Suggested .env content for docker-compose.cloud.yml on the ECS."
  sensitive   = true
  value       = <<-EOT
PUBLIC_ORIGIN=http://${huaweicloud_vpc_eip.elb.address}

DB_HOST=${try(huaweicloud_rds_instance.postgres.private_ips[0], "RDS_PRIVATE_IP")}
DB_PORT=5432
DB_NAME=${var.application_db_name}
DB_USER=${var.rds_admin_user}
DB_PASSWORD=${var.rds_admin_password}
DB_SSLMODE=require

HTTP_PORT=80

CLIENT_ID=${var.client_id}
CLIENT_SECRET=${var.client_secret}

JPA_DDL_AUTO=update
JWT_DURATION=${var.jwt_duration}
EOT
}
