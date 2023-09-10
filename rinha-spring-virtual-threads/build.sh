./gradlew build
docker build -t hstreb/rinha-spring-virtual-threads:0.0.1 .
./gradlew bootBuildImage --imageName=hstreb/rinha-spring-virtual-threads:0.0.1-native
