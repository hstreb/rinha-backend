./gradlew build
docker build -t hstreb/rinha-spring-virtual-threads:0.0.2 .
./gradlew bootBuildImage --imageName=hstreb/rinha-spring-virtual-threads:0.0.2-native
