# criar jvm
./gradlew build
docker build -t hstreb/rinha-spring-webflux:0.0.1 .
./gradlew bootBuildImage --imageName=hstreb/rinha-spring-webflux:0.0.1-native
