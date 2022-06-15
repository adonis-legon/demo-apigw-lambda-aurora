AWS_ENVIRONMENT=$1

# set environment variables for docker-compose
source docker-env-$AWS_ENVIRONMENT.sh

# run containers
docker compose down