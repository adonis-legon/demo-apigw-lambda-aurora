DEPLOY_ENVIRONMENT=$1

cd ../terraform

terraform workspace select $DEPLOY_ENVIRONMENT

terraform apply -destroy -var-file="$DEPLOY_ENVIRONMENT.auto.tfvars" -auto-approve

cd ../scripts