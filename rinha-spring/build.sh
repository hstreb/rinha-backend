# criar imagem docker
./gradlew build
docker build -t hstreb/rinha-spring:0.0.1 .
./gradlew bootBuildImage --imageName=hstreb/rinha-spring:0.0.1-native