JMETER_BIN_PATH=$1
ENVIRONMENT=$2
LOAD_TEST_ENV_FILE=load-test-env-$ENVIRONMENT.sh
LOAD_TEST_TEMPLATE_FILE=load-test-template.jmx
LOAD_TEST_TEMPLATE_FILE_TEMP=$ENVIRONMENT-$LOAD_TEST_TEMPLATE_FILE
LOAD_TEST_TEMPLATE_FILE_TEMP_OUTPUT=$LOAD_TEST_TEMPLATE_FILE_TEMP.log

# load environment variables
source $LOAD_TEST_ENV_FILE

# replace variables on the test template and create a temp load test file, for running the test
envsubst < $LOAD_TEST_TEMPLATE_FILE > $LOAD_TEST_TEMPLATE_FILE_TEMP

# run load test
sh $JMETER_BIN_PATH/jmeter -n -t $LOAD_TEST_TEMPLATE_FILE_TEMP -l $LOAD_TEST_TEMPLATE_FILE_TEMP_OUTPUT > /dev/null 2>&1 &

# watch test log
tail -fn 10 jmeter.log