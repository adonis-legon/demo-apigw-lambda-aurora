INIT_DB_ENV_FILE=$1
SCHEMA_FILE=$2

# set environment variables for corresponding database environment
. $INIT_DB_ENV_FILE

DB_CONN_MASTER=postgresql://$DB_MASTER_USERNAME:$DB_MASTER_PASSWORD@$DB_ENDPOINT:$DB_PORT/postgres

# create database if it does not exists
psql $DB_CONN_MASTER -c 'select 1' -d $DB_NAME &> /dev/null || \
psql $DB_CONN_MASTER -tc "CREATE DATABASE $DB_NAME"

DB_CONN_USER=postgresql://$DB_USERNAME:$DB_PASSWORD@$DB_ENDPOINT:$DB_PORT/$DB_NAME

# create user if it does not exists
psql $DB_CONN_USER -c 'select 1' -d $DB_NAME &> /dev/null || \
psql $DB_CONN_MASTER -tc "CREATE USER $DB_USERNAME WITH ENCRYPTED PASSWORD '$DB_PASSWORD'" || \
psql $DB_CONN_MASTER -tc "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME to $DB_USERNAME"

# create application schema
psql $DB_CONN_USER -f $SCHEMA_FILE