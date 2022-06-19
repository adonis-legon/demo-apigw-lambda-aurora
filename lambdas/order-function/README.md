# Lambda Function for performing Customer CRUD Operations in Python

1. Technology Stack:
1.1. Python 3.8
1.2. Postgresql connector
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

## Setup Local Environment (for python running/debugging)

### On Windows

```console
$ # create virtual environment
$ python -m venv .venv
$ # activate virtual environment
$ source .venv/Scripts/activate
$ # upgrade pip
$ python -m pip install --upgrade pip
$ # install dependencies
$ pip install -r requirements.txt

**Important:** after adding/updating/removing dependencies to the local virtual environment, run this command to update dependencies file

```console
$ pip freeze > requirements.txt
```
