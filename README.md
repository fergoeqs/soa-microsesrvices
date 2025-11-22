For windows: use build-run.ps1<br>
<br>
In other cases:<br>
docker compose down<br>
docker compose build<br>
docker-compose up config-service -d<br>
sleep 5<br>
curl http://localhost:8888/actuator/health<br>
docker-compose up eureka-service -d<br>
sleep 5<br>
curl http://localhost:8761/actuator/health<br>
docker-compose up -d<br>
