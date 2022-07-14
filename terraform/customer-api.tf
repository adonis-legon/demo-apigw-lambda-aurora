resource "aws_api_gateway_resource" "demo_apigw_lambda_aurora_api_resource_customer" {
  path_part   = "customer2"
  parent_id   = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.root_resource_id
  rest_api_id = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
}

resource "aws_api_gateway_method" "demo_apigw_lambda_aurora_api_post_method_customer" {
  rest_api_id   = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id   = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer.id
  http_method   = "POST"
  authorization = "NONE"
  api_key_required = true
}

resource "aws_api_gateway_integration" "demo_apigw_lambda_aurora_api_post_integration_customer" {
  rest_api_id             = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id             = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer.id
  http_method             = aws_api_gateway_method.demo_apigw_lambda_aurora_api_post_method_customer.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.demo_apigw_lambda_aurora_customer_function.invoke_arn
  credentials             = aws_iam_role.demo_apigw_lambda_aurora_api_role.arn
}

resource "aws_api_gateway_resource" "demo_apigw_lambda_aurora_api_resource_customer_id" {
  path_part   = "{id}"
  parent_id   = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer.id
  rest_api_id = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
}

resource "aws_api_gateway_method" "demo_apigw_lambda_aurora_api_get_method_customer" {
  rest_api_id   = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id   = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer_id.id
  http_method   = "GET"
  authorization = "NONE"
  api_key_required = true
}

resource "aws_api_gateway_integration" "demo_apigw_lambda_aurora_api_get_integration_customer" {
  rest_api_id             = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
  resource_id             = aws_api_gateway_resource.demo_apigw_lambda_aurora_api_resource_customer_id.id
  http_method             = aws_api_gateway_method.demo_apigw_lambda_aurora_api_get_method_customer.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.demo_apigw_lambda_aurora_customer_function.invoke_arn
  credentials             = aws_iam_role.demo_apigw_lambda_aurora_api_role.arn
}