resource "aws_iam_role" "demo_apigw_lambda_aurora_functions_role" {
  name = "${var.environment}-demo-apigw-lambda-aurora-functions-role"

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
    name = "${var.environment}-demo-apigw-lambda-aurora-functions-policy"

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

resource "aws_iam_role_policy_attachment" "demo_apigw_lambda_aurora_functions_role_vpc_access_execution" {
    role       = aws_iam_role.demo_apigw_lambda_aurora_functions_role.name
    policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"
}

resource "aws_iam_role_policy_attachment" "demo_apigw_lambda_aurora_functions_role_xray_write_only_access" {
    role       = aws_iam_role.demo_apigw_lambda_aurora_functions_role.name
    policy_arn = "arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess"
}