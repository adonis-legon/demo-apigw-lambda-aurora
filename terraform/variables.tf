variable "environment" {
  type = string
  description = "Environment identifier for the application infrastructure"
}

variable "aws_access_key" {
  type = string
  description = "AWS Access Key Id for performing IaC actions"
}

variable "aws_secret_key" {
  type = string
  description = "AWS Secret Key for performing IaC actions"
}

variable "aws_region" {
  type = string
  description = "AWS Region for the application infrastructure"
}

variable "aws_account" {
  type = string
  description = "AWS Account for the application infrastructure"
}

variable "vpc_id" {
    type = string
    description = "AWS VPC where all resources are deployed"
}

variable "db_subnets" {
    type = list(string)
    description = "AWS VPC subnets where the Aurora Serverless v2 Database Cluster is deployed"
}

variable "allowed_security_groups" {
    type = list(string)
    description = "Security Groups that can have access to the Database Cluster"
}

variable "min_aurora_capacity_units" {
    type = number
    description = "Minimum Aurora Capacity Units (ACU) for the Database Cluster to operate"
}

variable "max_aurora_capacity_units" {
    type = number
    description = "Maximum Aurora Capacity Units (ACU) for the Database Cluster to operate"
}

variable "db_instances" {
    default = {
        one = {}
    }
    description = "Specifications for each database instance of the Cluster"
}

variable "db_master_password" {
    type = string
    description = "Password for the root user of the Database Cluster"
}

variable "db_app_username" {
    type = string
    description = "Username defined by the application to connect to the Database"
}

variable "db_proxy_security_group" {
    type = string
    description = "Security Group where the Database Proxy belongs to"
}

variable "customer_function_name" {
    type = string
    description = "Name of the Customer Lambda Function"
}

variable "customer_function_version" {
    type = string
    description = "Version of the Customer Lambda Function"
}

variable "customer_function_dbname" {
    type = string
    description = "Database Name of the Customer Lambda Function"
}

variable "customer_function_timeout" {
    type = string
    description = "Timeout of the Customer Lambda Function"
}

variable "customer_function_spring_boot_name" {
    type = string
    description = "Name of the Customer Lambda Function (based on Spring Boot)"
}

variable "customer_function_spring_boot_version" {
    type = string
    description = "Version of the Customer Lambda Function (based on Spring Boot)"
}

variable "customer_function_spring_boot_dbname" {
    type = string
    description = "Database Name of the Customer Lambda Function (based on Spring Boot)"
}

variable "customer_function_spring_boot_timeout" {
    type = string
    description = "Timeout of the Customer Lambda Function (based on Spring Boot)"
}

variable "order_function_name" {
    type = string
    description = "Name of the Order Lambda Function"
}

variable "order_function_version" {
    type = string
    description = "Version of the Order Lambda Function"
}

variable "order_function_dbname" {
    type = string
    description = "Database Name of the Order Lambda Function"
}

variable "order_function_timeout" {
    type = string
    description = "Timeout of the Order Lambda Function"
}

variable "lambda_subnets" {
    type = list(string)
    description = "AWS VPC subnets where the Lambdas can have their ENI (Elastic Network Interface) to connect to the VPC"
}

variable "lambda_security_group" {
    type = string
    description = "Security Group for the Lambdas to be able to access the RDS Proxy"
}

variable "api_plan" {
    default = {
        api_key = "api_key"
        limit  = 100
        period = "DAY"
        burst_limit = 10
        rate_limit  = 5
    }
    description = "API settings for the demp plan"
}

variable "tags" {
  type = map(string)
  description = "Common tags for all resources."
}
