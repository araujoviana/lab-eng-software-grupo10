variable "access_key" {
  description = "HuaweiCloud access key. Prefer using environment variables instead of terraform.tfvars."
  type        = string
  default     = null
  sensitive   = true
}

variable "secret_key" {
  description = "HuaweiCloud secret key. Prefer using environment variables instead of terraform.tfvars."
  type        = string
  default     = null
  sensitive   = true
}

variable "region" {
  description = "HuaweiCloud region. ap-southeast-3 is Singapore."
  type        = string
  default     = "ap-southeast-3"
}

variable "availability_zone" {
  description = "Availability zone used by ECS, subnet, RDS and ELB. Adjust if this AZ is not available in your account."
  type        = string
  default     = "ap-southeast-3a"
}

variable "project_name" {
  description = "Prefix used in resource names."
  type        = string
  default     = "zelodesk"
}

variable "vpc_cidr" {
  description = "CIDR block for the application VPC."
  type        = string
  default     = "192.168.0.0/16"
}

variable "subnet_cidr" {
  description = "CIDR block for the application subnet."
  type        = string
  default     = "192.168.10.0/24"
}

variable "subnet_gateway_ip" {
  description = "Gateway IP for the application subnet."
  type        = string
  default     = "192.168.10.1"
}

variable "ssh_allowed_cidr" {
  description = "CIDR allowed to SSH into the ECS if you later attach a direct EIP to it. Keep this restricted."
  type        = string
  default     = "0.0.0.0/0"
}

variable "ecs_image_name" {
  description = "Public image name used by the ECS. Adjust according to images available in the Singapore region."
  type        = string
  default     = "Ubuntu 22.04 server 64bit"
}

variable "ecs_flavor_id" {
  description = "Small ECS flavor for low-traffic university/demo usage. Adjust if unavailable in the selected AZ."
  type        = string
  default     = "s6.small.1"
}

variable "ecs_admin_password" {
  description = "Administrative password for the ECS. HuaweiCloud usually requires upper/lowercase letters, numbers and special characters."
  type        = string
  sensitive   = true
}

variable "ecs_system_disk_type" {
  description = "ECS system disk type."
  type        = string
  default     = "GPSSD"
}

variable "ecs_system_disk_size" {
  description = "ECS system disk size in GB."
  type        = number
  default     = 40
}

variable "eip_bandwidth_size" {
  description = "Public EIP bandwidth size in Mbit/s."
  type        = number
  default     = 1
}

variable "ecs_ssh_eip_bandwidth_size" {
  description = "Administrative ECS EIP bandwidth size in Mbit/s, used only for SSH/Ansible."
  type        = number
  default     = 1
}

variable "jenkins_allowed_cidr" {
  description = "CIDR allowed to access Jenkins HTTP on port 8080. Use your IP/CIDR when possible."
  type        = string
  default     = "0.0.0.0/0"
}

variable "jenkins_flavor_id" {
  description = "ECS flavor for Jenkins. Builds need more memory than the app VM; adjust if unavailable in the selected AZ."
  type        = string
  default     = "s6.large.2"
}

variable "jenkins_system_disk_size" {
  description = "Jenkins ECS system disk size in GB."
  type        = number
  default     = 60
}

variable "jenkins_eip_bandwidth_size" {
  description = "Jenkins public EIP bandwidth size in Mbit/s."
  type        = number
  default     = 1
}

variable "rds_flavor" {
  description = "RDS PostgreSQL flavor. Adjust if unavailable or too expensive in Singapore."
  type        = string
  default     = "rds.pg.n1.large.2"
}

variable "rds_postgresql_version" {
  description = "RDS PostgreSQL major version."
  type        = string
  default     = "14"
}

variable "rds_admin_user" {
  description = "Expected PostgreSQL admin user for HuaweiCloud RDS PostgreSQL. Used for generated app env only."
  type        = string
  default     = "root"
}

variable "rds_admin_password" {
  description = "Administrative password for RDS PostgreSQL."
  type        = string
  sensitive   = true
}

variable "rds_volume_type" {
  description = "RDS disk type. ULTRAHIGH is SSD storage."
  type        = string
  default     = "CLOUDSSD"
}

variable "rds_volume_size" {
  description = "RDS storage size in GB. HuaweiCloud RDS minimum is usually 40 GB."
  type        = number
  default     = 40
}

variable "application_db_name" {
  description = "Database name for the application env file. HuaweiCloud RDS creates the postgres database by default; create zelodesk manually if you change this."
  type        = string
  default     = "postgres"
}

variable "client_id" {
  description = "OAuth client ID used by the application."
  type        = string
  default     = "zelodesk-client"
}

variable "client_secret" {
  description = "OAuth client secret used by the application."
  type        = string
  sensitive   = true
}

variable "jwt_duration" {
  description = "JWT duration in seconds."
  type        = number
  default     = 86400
}

variable "tags" {
  description = "Common tags applied to supported resources."
  type        = map(string)
  default = {
    managed_by = "terraform"
    project    = "zelodesk"
  }
}
