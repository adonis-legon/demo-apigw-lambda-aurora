APP_PATH=order-function
APP_NAME=demo-aws-lambda-order-function
IMAGE_VERSION=${1:-1.0.0}
PUSH_IMAGE=${2:-false}
AWS_ENVIRONMENT=${3}

. docker-build-python.sh $APP_PATH $APP_NAME $IMAGE_VERSION $PUSH_IMAGE $AWS_ENVIRONMENT