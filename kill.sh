docker-compose down
docker rm $(docker ps -a -q)
docker network rm rabo-net