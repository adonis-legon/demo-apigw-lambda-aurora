resource "aws_api_gateway_api_key" "demo_apigw_lambda_aurora_api_key" {
    name = local.api_key_name
    enabled = true
    value = var.api_key
}

resource "aws_api_gateway_usage_plan" "demo_apigw_lambda_aurora_api_usage_plan" {
    name         = local.api_plan_name
    description  = "Usage plan specific to the ${local.api_key_name} API key"
    quota_settings {
        limit  = "1000"
        period = "DAY"
    }
    throttle_settings {
        burst_limit = "10"
        rate_limit  = "5"
    }
    api_stages {
        api_id = aws_api_gateway_rest_api.demo_apigw_lambda_aurora_api.id
        stage  = aws_api_gateway_stage.demo_apigw_lambda_aurora_api_stage.stage_name
    }
}

resource "aws_api_gateway_usage_plan_key" "demo_apigw_lambda_aurora_api_usage_plan_key" {
    key_id        = aws_api_gateway_api_key.demo_apigw_lambda_aurora_api_key.id
    key_type      = "API_KEY"
    usage_plan_id = aws_api_gateway_usage_plan.demo_apigw_lambda_aurora_api_usage_plan.id
}