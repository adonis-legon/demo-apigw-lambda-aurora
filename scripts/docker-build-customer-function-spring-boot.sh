APP_PATH=customer-function-spring-boot
APP_NAME=demo-aws-lambda-customer-function-spring-boot
IMAGE_VERSION=${1:-1.0.0}
PUSH_IMAGE=${2:-false}
AWS_ENVIRONMENT=${3}

CURRENT_PATH=$(pwd)

# run unit and integration tests
cd ../lambdas/$APP_PATH
mvn test

# build app
mvn clean package -DskipTests

# build docker image
docker build -t $APP_NAME:$IMAGE_VERSION .

cd $CURRENT_PATH

# push image to AWS ECR if selected
. docker-push-aws.sh $AWS_ENVIRONMENT $PUSH_IMAGE