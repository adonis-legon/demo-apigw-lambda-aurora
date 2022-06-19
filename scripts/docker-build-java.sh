APP_PATH=$1
APP_NAME=$2
IMAGE_VERSION=${3:-1.0.0}
PUSH_IMAGE=${4:-false}
AWS_ENVIRONMENT=${5}

CURRENT_PATH=$(pwd)

# run unit and integration tests
cd ../lambdas/$APP_PATH
mvn test

# build app
mvn compile dependency:copy-dependencies -DincludeScope=runtime

# build docker image
docker build -t $APP_NAME:$IMAGE_VERSION .

cd $CURRENT_PATH

# push image to AWS ECR if selected
. docker-push-aws.sh $AWS_ENVIRONMENT $PUSH_IMAGE