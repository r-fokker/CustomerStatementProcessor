# Build project
mvn clean package
# Build application image and container
docker build . -t rabo
