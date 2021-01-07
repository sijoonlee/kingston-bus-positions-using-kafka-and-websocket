docker-compose up -d postgres
docker-compose stop postgres
docker system prune --volumes

sudo docker logs [containerid]

docker rm -f -v [containerid] // clear volumn

docker-compose down --volumes // clear volumn

docker-compose down --rmi all --volumes // clear volumns images


curl -X GET http://localhost:8083/ 


docker-compose exec broker kafka-console-consumer --bootstrap-server localhost:9092 --topic transit-vehicles  --from-beginning
docker-compose exec broker kafka-topics --list --zookeeper zookeeper:2181
