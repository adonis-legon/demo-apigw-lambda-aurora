resource "aws_db_parameter_group" "demo_apigw_lambda_aurora_db" {
  name        = "${var.environment}-demo-apigw-lambda-aurora-db-parameter-group"
  family      = "aurora-postgresql13"
  description = "${var.environment}-demo-apigw-lambda-aurora-db-parameter-group"
  tags        = var.tags
}

resource "aws_rds_cluster_parameter_group" "demo_apigw_lambda_aurora_db" {
  name        = "${var.environment}-demo-apigw-lambda-aurora-cluster-parameter-group"
  family      = "aurora-postgresql13"
  description = "${var.environment}-demo-apigw-lambda-aurora-cluster-parameter-group"
  tags        = var.tags
}

module "aurora_postgresql_serverlessv2" {
  source = "terraform-aws-modules/rds-aurora/aws"
  name   = "${var.environment}-demo-apigw-lambda-aurora-db"

  engine            = "aurora-postgresql"
  engine_version    = "13.6"
  engine_mode       = "provisioned"
  instance_class    = "db.serverless"
  instances         = var.db_instances
  storage_encrypted = true

  vpc_id                    = var.vpc_id
  create_db_subnet_group    = true
  db_subnet_group_name      = "${var.environment}-demo-apigw-lambda-aurora-sng"
  subnets                   = var.db_subnets
  allowed_security_groups   = var.allowed_security_groups

  serverlessv2_scaling_configuration = {
    min_capacity = var.min_aurora_capacity_units
    max_capacity = var.max_aurora_capacity_units
  }

  apply_immediately     = true
  skip_final_snapshot   = true

  db_parameter_group_name                       = aws_db_parameter_group.demo_apigw_lambda_aurora_db.id
  db_cluster_db_instance_parameter_group_name   = aws_rds_cluster_parameter_group.demo_apigw_lambda_aurora_db.id

  create_random_password = false
  master_password = var.db_master_password

  tags = var.tags
}
