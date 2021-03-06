# Native-Image Profiling

Build the Apps and Docker Images:
```
mvn package -Pnative-server,native-client &&
docker build -f quarkus/src/main/docker/Dockerfile.jvm -t quarkus-jvm . &&
docker build -f quarkus/src/main/docker/Dockerfile.native -t quarkus-native . &&
docker build -f quarkus-isolates/src/main/docker/Dockerfile.native -t quarkus-isolates-native . &&
docker build -f springboot/src/main/docker/Dockerfile.jvm -t springboot-jdk .
```

```
mvn package -Pnative-server &&
docker build -f quarkus/src/main/docker/Dockerfile.jvm -t quarkus-jvm . &&
docker build -f quarkus/src/main/docker/Dockerfile.native -t quarkus-native .
```

Start INFRASTRUCTURE (Grafana / Prometheus / MongoDB / cAdvisor):
```
docker compose up
```

Run Reactive Quarkus Native Server:
```
docker run -it --memory 200MB --cpus 2 --network host --name server quarkus-native
```

Open cAdvisor: http://localhost:8081