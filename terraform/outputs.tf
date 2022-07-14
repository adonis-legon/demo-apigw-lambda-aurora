output "db_custer_endpoint" {
    value = module.aurora_postgresql_serverlessv2.cluster_endpoint
}

output "db_proxy_endpoint" {
    value = aws_db_proxy.demo_apigw_lambda_aurora_db_proxy.endpoint
}

output "api_gateway_endpoint" {
  value = aws_api_gateway_stage.demo_apigw_lambda_aurora_api_stage.invoke_url
}