output "db_custer_endpoint" {
    value = module.aurora_postgresql_serverlessv2.cluster_endpoint
}

output "db_proxy_endpoint" {
    value = aws_db_proxy.demo_apigw_lambda_aurora_db_proxy.endpoint
}