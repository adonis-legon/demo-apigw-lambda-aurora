resource "aws_iam_role" "demo_apigw_lambda_aurora_customer_function_role" {
  name = "${var.environment}-demo-apigw-lambda-customer-function-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
        Effect = "Allow"
        Sid = ""
      }
    ]
  })

  inline_policy {
    name = "${var.environment}-demo-apigw-lambda-customer-function-policy"

    policy = jsonencode({
      Version = "2012-10-17"
      Statement = [
        {
            Action = [
                "rds-db:connect"
            ],
            Resource = "arn:aws:rds-db:${var.aws_region}:${var.aws_account}:dbuser:*/${var.db_app_username}",
            Effect = "Allow"
        },
        {
            Action: [
                "logs:CreateLogGroup",
            ],
            Resource: "*",
            Effect = "Deny"
        }
      ]
    })
  }

  tags = var.tags
}

resource "aws_iam_role_policy_attachment" "demo_apigw_lambda_aurora_customer_function_role_vpc_access_execution" {
    role       = aws_iam_role.demo_apigw_lambda_aurora_customer_function_role.name
    policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"
}

resource "aws_iam_role_policy_attachment" "demo_apigw_lambda_aurora_customer_function_role_xray_write_only_access" {
    role       = aws_iam_role.demo_apigw_lambda_aurora_customer_function_role.name
    policy_arn = "arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess"
}

locals {
  customer_function_name = "${var.environment}-${var.customer_function_name}"
}

resource "aws_lambda_function" "demo_apigw_lambda_aurora_customer_function" {
  function_name  = local.customer_function_name
  description    = "Customer Lambda Function for the Demo API Gateway + Lambda + Aurora Project"

  role           = aws_iam_role.demo_apigw_lambda_aurora_customer_function_role.arn

  image_uri      = "${var.aws_account}.dkr.ecr.${var.aws_region}.amazonaws.com/${var.customer_function_name}:${var.customer_function_version}"
  package_type   = "Image"
  publish        = true

  architectures  = [ "x86_64" ]
  memory_size    = 256

  vpc_config {
    subnet_ids = var.lambda_subnets
    security_group_ids = [ var.lambda_security_group ]
  }

  timeout = var.customer_function_timeout
  tracing_config {
    mode = "Active"
  }

  environment {
    variables = {
        DB_ENDPOINT = aws_db_proxy.demo_apigw_lambda_aurora_db_proxy.endpoint
        DB_PORT = 5432
        DB_USER = var.db_app_username
        DB_NAME = var.customer_function_dbname
        DB_AUTH_IAM = true
        DB_REGION = var.aws_region
        POWERTOOLS_SERVICE_NAME = local.customer_function_name
    }
  }

  tags = var.tags
}

resource "aws_lambda_alias" "demo_apigw_lambda_aurora_customer_function_alias" {
  name             = "${var.environment}-${var.customer_function_name}-life"
  function_name    = aws_lambda_function.demo_apigw_lambda_aurora_customer_function.arn
  function_version = "$LATEST"
}

resource "aws_cloudwatch_log_group" "demo_apigw_lambda_aurora_customer_function_logs" {
  name              = "/aws/lambda/${aws_lambda_function.demo_apigw_lambda_aurora_customer_function.function_name}"
  retention_in_days = 5

  tags = var.tags
}