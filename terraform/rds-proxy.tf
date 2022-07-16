resource "aws_iam_role" "demo_apigw_lambda_aurora_db_proxy_role" {
    name = "${var.environment}-demo-apigw-lambda-db-proxy-role"
    assume_role_policy = jsonencode({
        Statement = [{
            Effect  = "Allow"
            Action = "sts:AssumeRole"
            Principal = {
                Service = "rds.amazonaws.com"
            }
        }]
    })

    inline_policy {
      name = "${var.environment}-demo-apigw-lambda-db-proxy-role-policy"
      policy = jsonencode({
            Statement = [{
                Action   = ["secretsmanager:GetSecretValue", "secretsmanager:DescribeSecret"]
                Effect   = "Allow"
                Resource = aws_secretsmanager_secret.demo_apigw_lambda_aurora_db_user_password.id
            }]
        })
    }

    tags = var.tags
}

resource "aws_db_proxy" "demo_apigw_lambda_aurora_db_proxy" {
    name            = "${var.environment}-demo-apigw-lambda-aurora-db-proxy"
    debug_logging   = false
    engine_family   = "POSTGRESQL"

    idle_client_timeout = 18000
    require_tls         = true

    role_arn                = aws_iam_role.demo_apigw_lambda_aurora_db_proxy_role.arn
    vpc_subnet_ids          = var.db_subnets
    vpc_security_group_ids  = [var.db_proxy_security_group]

    auth {
      auth_scheme   = "SECRETS"
      description   = "Db Authentication Credentials"
      iam_auth      = "REQUIRED"
      secret_arn    = aws_secretsmanager_secret.demo_apigw_lambda_aurora_db_user_password.arn
    }

    tags = var.tags
}

resource "aws_db_proxy_default_target_group" "demo_apigw_lambda_aurora_db_proxy_default_target_group" {
  db_proxy_name = aws_db_proxy.demo_apigw_lambda_aurora_db_proxy.name

  connection_pool_config {
    connection_borrow_timeout    = 120
    init_query                   = "SET CLIENT_ENCODING TO 'UTF8'"
    max_connections_percent      = 100
    max_idle_connections_percent = 50
    session_pinning_filters      = ["EXCLUDE_VARIABLE_SETS"]
  }
}

# workaround to avoid Aurora Cluster's primary instance to not be in the Available state, 
# before Terraform tries to create the RDS Proxy Target, that depends on the instances availability
resource "time_sleep" "demo_apigw_lambda_aurora_db_proxy_target_group_wait_time" {
  create_duration  = "10m"
  depends_on            = [
    module.aurora_postgresql_serverlessv2.cluster_id
  ]
}

resource "aws_db_proxy_target" "demo_apigw_lambda_aurora_db_proxy_target_group" {
  db_cluster_identifier   = module.aurora_postgresql_serverlessv2.cluster_id
  db_proxy_name           = aws_db_proxy.demo_apigw_lambda_aurora_db_proxy.name
  target_group_name       = aws_db_proxy_default_target_group.demo_apigw_lambda_aurora_db_proxy_default_target_group.name

  depends_on              = [
    time_sleep.demo_apigw_lambda_aurora_db_proxy_target_group_wait_time
  ]
}