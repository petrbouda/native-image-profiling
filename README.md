# Native-Image Profiling

### Install TOOLS

Install PSRECORD tool (visualization tool)
```
sudo apt install python3-pip
pip install psrecord
pip install matplotlib
```

### Build APP

Build the project:
```
mvn package -Pnative-server,native-client
```

Create Docker Images:
```
docker build -f reactive/src/main/docker/Dockerfile.jvm -t quarkus-reactive-jvm . &&
docker build -f reactive/src/main/docker/Dockerfile.native -t quarkus-reactive-native . &&
docker build -f imperative/src/main/docker/Dockerfile.jvm -t quarkus-imperative-jvm . &&
docker build -f imperative/src/main/docker/Dockerfile.native -t quarkus-imperative-native . &&
docker build -f springboot/src/main/docker/Dockerfile.jvm -t springboot-jdk .
```

Run Mongo Server:
```
docker run -ti --rm -p 27017:27017 mongo:4.0
```

Run Reactive Quarkus Native Server:
```
docker rm server && docker run -it --memory 200MB --cpus 2 --network host --name server quarkus-reactive-jvm
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