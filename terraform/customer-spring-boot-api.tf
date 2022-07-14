resource "aws_api_gateway_resource" "demo_apigw_lambda_aurora_api_resource_customer_spring_boot" {
  path_part   = "customer"
  parent_id   = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.root_resource_id
  rest_api_id = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
}

resource "aws_api_gateway_method" "demo_apigw_lambda_aurora_api_post_method_customer_spring_boot" {
  rest_api_id   = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id   = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer_spring_boot.id
  http_method   = "POST"
  authorization = "NONE"
  api_key_required = true
}

resource "aws_api_gateway_integration" "demo_apigw_lambda_aurora_api_post_integration_customer_spring_boot" {
  rest_api_id             = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id             = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer_spring_boot.id
  http_method             = aws_api_gateway_method.demo_apigw_lambda_aurora_api_post_method_customer_spring_boot.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.demo_apigw_lambda_aurora_customer_function_spring_boot.invoke_arn
  credentials             = aws_iam_role.demo_apigw_lambda_aurora_api_role.arn
}

resource "aws_api_gateway_resource" "demo_apigw_lambda_aurora_api_resource_customer_spring_boot_id" {
  path_part   = "{id}"
  parent_id   = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer_spring_boot.id
  rest_api_id = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
}

resource "aws_api_gateway_method" "demo_apigw_lambda_aurora_api_get_method_customer_spring_boot" {
  rest_api_id   = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id   = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer_spring_boot_id.id
  http_method   = "GET"
  authorization = "NONE"
  api_key_required = true
}

resource "aws_api_gateway_integration" "demo_apigw_lambda_aurora_api_get_integration_customer_spring_boot" {
  rest_api_id             = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id             = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer_spring_boot_id.id
  http_method             = aws_api_gateway_method.demo_apigw_lambda_aurora_api_get_method_customer_spring_boot.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.demo_apigw_lambda_aurora_customer_function_spring_boot.invoke_arn
  credentials             = aws_iam_role.demo_apigw_lambda_aurora_api_role.arn
}