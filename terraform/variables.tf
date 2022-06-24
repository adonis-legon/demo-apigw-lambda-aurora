variable "environment" {
  type = string
}

variable "aws_access_key" {
  type = string
}

variable "aws_secret_key" {
  type = string
}

variable "aws_region" {
  type = string
}

variable "aws_account" {
  type = string
}

variable "vpc_id" {
    type = string
}

variable "db_subnets" {
    type = list(string)
}

variable "allowed_security_groups" {
    type = list(string)
}

variable "min_aurora_capacity_units" {
    type = number
}

variable "max_aurora_capacity_units" {
    type = number
}

variable "db_instances" {
    default = {
        one = {}
    }
}

variable "db_master_password" {
    type = string
}

variable "lambda_subnets" {
    type = list(string)
}

variable "tags" {
  type = map(string)
  description = "Common tags for all resources."
}
