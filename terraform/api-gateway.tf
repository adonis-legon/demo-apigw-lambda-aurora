locals {
  api_name = "${var.environment}-demo-apigw-lambda-aurora-api"
  api_key_name = "${var.environment}-demo-apigw-lambda-aurora-api-key"
  api_plan_name = "${var.environment}-demo-apigw-lambda-aurora-api-plan"
}

resource "aws_api_gateway_rest_api" "demo_apigw_lambda_aurora_api" {
  name = local.api_name
  tags = var.tags
}

resource "aws_iam_role" "demo_apigw_lambda_aurora_api_role" {
  name = "${local.api_name}-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "apigateway.amazonaws.com"
        }
      },
    ]
  })
  path = "/"
  inline_policy {
    name = "${local.api_name}-permission"

    policy = jsonencode({
      Version = "2012-10-17"
      Statement = [
        {
          Action   = "lambda:InvokeFunction"
          Effect   = "Allow"
          Resource = [
            aws_lambda_function.demo_apigw_lambda_aurora_customer_function.arn,
            aws_lambda_function.demo_apigw_lambda_aurora_customer_function_spring_boot.arn,
            aws_lambda_function.demo_apigw_lambda_aurora_order_function.arn
          ]
        },
      ]
    })
  }

  tags = var.tags
}

resource "aws_api_gateway_deployment" "demo_apigw_lambda_aurora_api_deployment" {
  rest_api_id = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id

  # NOTE: this is a hack to make terraform detect a change and always redeploy the API, 
  # otherwise it won´t deploy new integrations and/or method changes
  stage_description = "Deployed at ${timestamp()}"

  lifecycle {
    create_before_destroy = true
  }

  depends_on = [
    aws_api_gateway_integration.demo_apigw_lambda_aurora_api_post_integration_order,
    aws_api_gateway_integration.demo_apigw_lambda_aurora_api_get_integration_order,
    aws_api_gateway_integration.demo_apigw_lambda_aurora_api_post_integration_customer,
    aws_api_gateway_integration.demo_apigw_lambda_aurora_api_get_integration_customer,
    aws_api_gateway_integration.demo_apigw_lambda_aurora_api_post_integration_customer_spring_boot,
    aws_api_gateway_integration.demo_apigw_lambda_aurora_api_get_integration_customer_spring_boot,
  ]
}

resource "aws_api_gateway_stage" "demo_apigw_lambda_aurora_api_stage" {
  deployment_id = aws_api_gateway_deployment.demo_apigw_lambda_aurora_api_deployment.id
  rest_api_id   = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  stage_name    = var.environment
  xray_tracing_enabled = true

  tags = var.tags
}