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