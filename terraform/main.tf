locals {
  name = var.project_name
}

resource "huaweicloud_vpc" "main" {
  name = "${local.name}-vpc"
  cidr = var.vpc_cidr
  tags = var.tags
}

resource "huaweicloud_vpc_subnet" "app" {
  name              = "${local.name}-subnet"
  cidr              = var.subnet_cidr
  gateway_ip        = var.subnet_gateway_ip
  vpc_id            = huaweicloud_vpc.main.id
  availability_zone = var.availability_zone
  tags              = var.tags
}

resource "huaweicloud_networking_secgroup" "app" {
  name                 = "${local.name}-ecs-sg"
  description          = "Security group for the ${local.name} ECS"
  delete_default_rules = true
  tags                 = var.tags
}

resource "huaweicloud_networking_secgroup_rule" "app_egress_all" {
  security_group_id = huaweicloud_networking_secgroup.app.id
  direction         = "egress"
  ethertype         = "IPv4"
  remote_ip_prefix  = "0.0.0.0/0"
}

resource "huaweicloud_networking_secgroup_rule" "app_ssh" {
  security_group_id = huaweicloud_networking_secgroup.app.id
  direction         = "ingress"
  ethertype         = "IPv4"
  protocol          = "tcp"
  port_range_min    = 22
  port_range_max    = 22
  remote_ip_prefix  = var.ssh_allowed_cidr
}

resource "huaweicloud_networking_secgroup_rule" "app_http_from_vpc" {
  security_group_id = huaweicloud_networking_secgroup.app.id
  direction         = "ingress"
  ethertype         = "IPv4"
  protocol          = "tcp"
  port_range_min    = 80
  port_range_max    = 80
  remote_ip_prefix  = var.vpc_cidr
}

resource "huaweicloud_networking_secgroup" "rds" {
  name                 = "${local.name}-rds-sg"
  description          = "Security group for the ${local.name} PostgreSQL RDS instance"
  delete_default_rules = true
  tags                 = var.tags
}

resource "huaweicloud_networking_secgroup_rule" "rds_egress_all" {
  security_group_id = huaweicloud_networking_secgroup.rds.id
  direction         = "egress"
  ethertype         = "IPv4"
  remote_ip_prefix  = "0.0.0.0/0"
}

resource "huaweicloud_networking_secgroup_rule" "rds_postgres_from_app" {
  security_group_id = huaweicloud_networking_secgroup.rds.id
  direction         = "ingress"
  ethertype         = "IPv4"
  protocol          = "tcp"
  port_range_min    = 5432
  port_range_max    = 5432
  remote_group_id   = huaweicloud_networking_secgroup.app.id
}

resource "huaweicloud_compute_instance" "app" {
  name               = "${local.name}-ecs"
  image_name         = var.ecs_image_name
  flavor_id          = var.ecs_flavor_id
  admin_pass         = var.ecs_admin_password
  security_group_ids = [huaweicloud_networking_secgroup.app.id]
  availability_zone  = var.availability_zone
  system_disk_type   = var.ecs_system_disk_type
  system_disk_size   = var.ecs_system_disk_size
  charging_mode      = "postPaid"

  network {
    uuid = huaweicloud_vpc_subnet.app.id
  }

  tags = var.tags
}

resource "huaweicloud_vpc_eip" "ecs_ssh" {
  name = "${local.name}-ecs-ssh-eip"

  publicip {
    type = "5_bgp"
  }

  bandwidth {
    name        = "${local.name}-ecs-ssh-bandwidth"
    share_type  = "PER"
    size        = var.ecs_ssh_eip_bandwidth_size
    charge_mode = "traffic"
  }

  tags = var.tags
}

resource "huaweicloud_compute_eip_associate" "ecs_ssh" {
  public_ip   = huaweicloud_vpc_eip.ecs_ssh.address
  instance_id = huaweicloud_compute_instance.app.id
}

resource "huaweicloud_networking_secgroup" "jenkins" {
  name                 = "${local.name}-jenkins-sg"
  description          = "Security group for the ${local.name} Jenkins ECS"
  delete_default_rules = true
  tags                 = var.tags
}

resource "huaweicloud_networking_secgroup_rule" "jenkins_egress_all" {
  security_group_id = huaweicloud_networking_secgroup.jenkins.id
  direction         = "egress"
  ethertype         = "IPv4"
  remote_ip_prefix  = "0.0.0.0/0"
}

resource "huaweicloud_networking_secgroup_rule" "jenkins_ssh" {
  security_group_id = huaweicloud_networking_secgroup.jenkins.id
  direction         = "ingress"
  ethertype         = "IPv4"
  protocol          = "tcp"
  port_range_min    = 22
  port_range_max    = 22
  remote_ip_prefix  = var.ssh_allowed_cidr
}

resource "huaweicloud_networking_secgroup_rule" "jenkins_http" {
  security_group_id = huaweicloud_networking_secgroup.jenkins.id
  direction         = "ingress"
  ethertype         = "IPv4"
  protocol          = "tcp"
  port_range_min    = 8080
  port_range_max    = 8080
  remote_ip_prefix  = var.jenkins_allowed_cidr
}

resource "huaweicloud_compute_instance" "jenkins" {
  name               = "${local.name}-jenkins-ecs"
  image_name         = var.ecs_image_name
  flavor_id          = var.jenkins_flavor_id
  admin_pass         = var.ecs_admin_password
  security_group_ids = [huaweicloud_networking_secgroup.jenkins.id]
  availability_zone  = var.availability_zone
  system_disk_type   = var.ecs_system_disk_type
  system_disk_size   = var.jenkins_system_disk_size
  charging_mode      = "postPaid"

  network {
    uuid = huaweicloud_vpc_subnet.app.id
  }

  tags = var.tags
}

resource "huaweicloud_vpc_eip" "jenkins" {
  name = "${local.name}-jenkins-eip"

  publicip {
    type = "5_bgp"
  }

  bandwidth {
    name        = "${local.name}-jenkins-bandwidth"
    share_type  = "PER"
    size        = var.jenkins_eip_bandwidth_size
    charge_mode = "traffic"
  }

  tags = var.tags
}

resource "huaweicloud_compute_eip_associate" "jenkins" {
  public_ip   = huaweicloud_vpc_eip.jenkins.address
  instance_id = huaweicloud_compute_instance.jenkins.id
}

resource "huaweicloud_rds_instance" "postgres" {
  name              = "${local.name}-postgres"
  flavor            = var.rds_flavor
  vpc_id            = huaweicloud_vpc.main.id
  subnet_id         = huaweicloud_vpc_subnet.app.id
  security_group_id = huaweicloud_networking_secgroup.rds.id
  availability_zone = [var.availability_zone]
  charging_mode     = "postPaid"

  db {
    type     = "PostgreSQL"
    version  = var.rds_postgresql_version
    password = var.rds_admin_password
    port     = 5432
  }

  volume {
    type = var.rds_volume_type
    size = var.rds_volume_size
  }

  backup_strategy {
    start_time = "08:00-09:00"
    keep_days  = 1
  }

  tags = var.tags
}

resource "huaweicloud_vpc_eip" "elb" {
  name = "${local.name}-elb-eip"

  publicip {
    type = "5_bgp"
  }

  bandwidth {
    name        = "${local.name}-elb-bandwidth"
    share_type  = "PER"
    size        = var.eip_bandwidth_size
    charge_mode = "traffic"
  }

  tags = var.tags
}

resource "huaweicloud_elb_loadbalancer" "public" {
  name              = "${local.name}-elb"
  description       = "Public load balancer for ${local.name}"
  vpc_id            = huaweicloud_vpc.main.id
  ipv4_subnet_id    = huaweicloud_vpc_subnet.app.ipv4_subnet_id
  ipv4_eip_id       = huaweicloud_vpc_eip.elb.id
  availability_zone = [var.availability_zone]
  charging_mode     = "postPaid"
  tags              = var.tags
}

resource "huaweicloud_elb_listener" "http" {
  name            = "${local.name}-http"
  protocol        = "HTTP"
  protocol_port   = 80
  loadbalancer_id = huaweicloud_elb_loadbalancer.public.id

  idle_timeout     = 60
  request_timeout  = 60
  response_timeout = 60

  forward_host  = true
  forward_proto = true
  real_ip       = true

  tags = var.tags
}

resource "huaweicloud_elb_pool" "app" {
  name        = "${local.name}-pool"
  protocol    = "HTTP"
  lb_method   = "ROUND_ROBIN"
  listener_id = huaweicloud_elb_listener.http.id
  type        = "instance"
  vpc_id      = huaweicloud_vpc.main.id
}

resource "huaweicloud_elb_monitor" "app" {
  name             = "${local.name}-http-monitor"
  pool_id          = huaweicloud_elb_pool.app.id
  protocol         = "HTTP"
  interval         = 30
  timeout          = 5
  max_retries      = 3
  max_retries_down = 3
  url_path         = "/"
  status_code      = "200-399"
}

resource "huaweicloud_elb_member" "app" {
  name          = "${local.name}-ecs-member"
  pool_id       = huaweicloud_elb_pool.app.id
  subnet_id     = huaweicloud_vpc_subnet.app.ipv4_subnet_id
  address       = huaweicloud_compute_instance.app.network[0].fixed_ip_v4
  protocol_port = 80
  weight        = 1
}
