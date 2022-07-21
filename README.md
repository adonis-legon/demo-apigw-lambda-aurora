# Demo Project for and AWS ApiGateway+Lambda+Aurora stack with security features

## Docker Build Locally

```console
scripts$ . docker-build-[function-name].sh <version>
scripts$ # example
scripts$ . docker-build-customer-function.sh 1.0.0
```

## Docker Build Locally and Push to AWS ECR

**Important:** before running the push to AWS ECR feature, there must be a local/custom docker-env-[env].sh to have the correct AWS Account and User information

```console
scripts$ . docker-build-[function-name].sh <version> <push-image> <aws-environment>
scripts$ # example
scripts$ . docker-build-customer-function.sh 1.0.0 true dev
```

## Docker Start Locally

```console
scripts$ . docker-start-[function-name].sh <version>
scripts$ # example
scripts$ . docker-start-customer-function.sh 1.0.0
```

## Docker Function Invoke Locally (using Runtime Interfase Emulator or RIE)
```console
scripts$ curl -XPOST "http://localhost:[function-port]/2015-03-31/functions/function/invocations" -d "@[test-event-path]"
scripts$ # example
scripts$ curl -XPOST "http://localhost:9001/2015-03-31/functions/function/invocations" -d "@../lambdas/customer-function/src/test/resources/create-customer/events/ok.json"
```

## Setup Terraform

**Important:** before running terraform scripts, there must be a local/custom [env].auto.tfvars file with the specific configurations of your deployment

```console
terraform$ terraform workspace new <env>
terraform$ terraform init
```

## Review Infrastructure changes

```console
scripts$ . terraform-plan.sh <env>
```

## Apply Infrastructure changes

```console
scripts$ . terraform-apply.sh <env>
```

## Manual DB Initialization

1. Create an environment variables file for the corresponding Database, using the *db-env-template.sh* file as a template
2. Run the *init-db.sh* script passing the environment file and the application schema file as arguments. Example:

```console
scripts$ . init-db.sh customer-dev.sh customer.sql
```

## Destroy Infrastructure

```console
scripts$ . terraform-destroy.sh <env>
```

## E2E Test APIs

```console
$ curl --request POST 'https://<api-gateway-endpoint>/<stage>/order' --header 'x-api-key: <api-key>' --header 'Content-Type: application/json' \
--data-raw '{"customer_name": "Demo User", "total_amount": 1234.50}'
```

## Load Tests APIs

**Important:** before running load tests, there must be a local/custom locad-test-env-[env].sh file with the specific configurations of your load test and environment

```console
scripts$ . load-test-start.sh /home/user/apache-jmeter-5.4.1/bin dev
```