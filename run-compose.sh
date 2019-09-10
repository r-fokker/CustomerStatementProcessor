docker-compose down
docker network create rabo-net
docker-compose -f ./docker-compose.yml up --remove-orphans -d