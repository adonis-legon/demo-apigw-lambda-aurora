AWS_ENVIRONMENT=$1
PUSH_IMAGE=${2:-false}
    
if [ "$PUSH_IMAGE" = true ]; then
    # set environment variables for AWS Account
    . docker-env-$AWS_ENVIRONMENT.sh

    # login to AWS ECR
    aws ecr get-login-password --region $AWS_REGION --profile $AWS_PROFILE | docker login --username AWS --password-stdin $AWS_ACCOUNT.dkr.ecr.$AWS_REGION.amazonaws.com

    # create private repo if not exists
    aws ecr describe-repositories --profile $AWS_PROFILE --repository-names $APP_NAME || aws ecr create-repository --profile $AWS_PROFILE --repository-name $APP_NAME

    # tag image for AWS ECR repo
    docker tag $APP_NAME:$IMAGE_VERSION $AWS_ACCOUNT.dkr.ecr.$AWS_REGION.amazonaws.com/$APP_NAME:$IMAGE_VERSION

    # push image to AWS ECR repo
    docker push $AWS_ACCOUNT.dkr.ecr.$AWS_REGION.amazonaws.com/$APP_NAME:$IMAGE_VERSION
fi