APP_PATH=$1
APP_NAME=$2
IMAGE_VERSION=${3:-1.0.0}
PUSH_IMAGE=${4:-false}
AWS_ENVIRONMENT=${5}

CURRENT_PATH=$(pwd)

# run unit and integration tests
cd ../lambdas/$APP_PATH

# NOTE: this way to activate virtual environmnet is for Windows Host, in case of Linux or MacOS use:
# $source .venv/bin/activate 
source .venv/Scripts/activate
cd src/test
pytest
cd ../../

# build docker image
docker build -t $APP_NAME:$IMAGE_VERSION .

cd $CURRENT_PATH

# push image to AWS ECR if selected
. docker-push-aws.sh $AWS_ENVIRONMENT $PUSH_IMAGE