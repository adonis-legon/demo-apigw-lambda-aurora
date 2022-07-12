FROM public.ecr.aws/lambda/java:11-arm64

# Copy function code and runtime dependencies from Maven layout
COPY target/classes ${LAMBDA_TASK_ROOT}
COPY target/lib/* ${LAMBDA_TASK_ROOT}/lib/

CMD [ "com.example.StreamLambdaHandler::handleRequest" ]