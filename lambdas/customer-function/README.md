# Lambda Function for performing Customer CRUD Operations

1. Technology Stack:
1.1. Java 11
1.2. Basic JDBC with Postgresql Driver
2. Observability:
2.1. Logging with AWS Cloudwatch Logs
2.2. Metrics with AWS Cloudwatch Metrics
2.3. Tracing with AWS XRay

## Configurations

*Note: All configurations should be defined as environment variables for the Lambda function*

DB_ENDPOINT:    Database endpoint (e.g. IP or FQDN)
DB_PORT:        Database port (default: 5432)
DB_USER:        Database user
DB_PASS:        Database password (only when using user/password authentication, for IAM authentication it is not needed)
DB_NAME:        Database name
DB_AUTH_IAM:    If the function will use IAM based authentication to access de Database or not (values: true or false)
DB_REGION:      AWS Region where the RDS service belongs to

## Reference implementation

- https://medium.com/i-love-my-local-farmer-engineering-blog/how-to-use-java-in-your-db-connected-aws-lambdas-211c1f9c53aa
- https://medium.com/i-love-my-local-farmer-engineering-blog/connecting-your-java-aws-lambda-to-an-rds-database-and-rds-proxy-4512a3ba1c3d