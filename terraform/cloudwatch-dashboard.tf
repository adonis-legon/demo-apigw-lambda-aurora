resource "aws_cloudwatch_dashboard" "demo_apigw_lambda_aurora_dashboard" {
  dashboard_name = "${var.environment}-demo-apigw-lambda-aurora"
  dashboard_body = "${file("cloudwatch-dashboard.json")}"
}