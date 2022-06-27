resource "random_password" "db_user_password"{
    length              = 16
    special             = true
    override_special    = "_%@"
}

resource "aws_secretsmanager_secret" "demo_apigw_lambda_aurora_db_user_password"{
    name = "${var.environment}-demo-apigw-lambda-db-user-password"
    tags = var.tags
}

resource "aws_secretsmanager_secret_version" "demo_apigw_lambda_aurora_db_user_password" {
    secret_id       = aws_secretsmanager_secret.demo_apigw_lambda_aurora_db_user_password.id
    secret_string   = jsonencode({
        username = var.db_app_username
        password = random_password.db_user_password.result
    })
}