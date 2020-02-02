# Native-Image Profiling

### Install TOOLS

Install PSRECORD tool (visualization tool)
```
sudo apt install python3-pip
pip install psrecord
pip install matplotlib
```

### Build APP

Build the Apps and Docker Images:
```
mvn package -Pnative-server,native-client &&
docker build -f quarkus/src/main/docker/Dockerfile.jvm -t quarkus-jvm . &&
docker build -f quarkus/src/main/docker/Dockerfile.native -t quarkus-native . &&
docker build -f springboot/src/main/docker/Dockerfile.jvm -t springboot-jdk .
```

Start INFRASTRUCTURE (Grafana / Prometheus / MongoDB):
```
docker compose up
```

Run Reactive Quarkus Native Server:
```
docker rm server && docker run -it --memory 200MB --cpus 2 --network host --name server quarkus-jvm
```

Get PID (see what is the mapped PID from docker):
```
# Grep from ps according to a full process name
pgrep -f server-test-application

# Docker way
docker top server
```

Connect PSRECORD: 
```
psrecord $(pgrep -f server-test-application) --plot application.png
```