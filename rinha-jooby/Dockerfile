FROM eclipse-temurin:17.0.8_7-jdk
WORKDIR /rinha-jooby
COPY build/libs/rinha-jooby-0.0.1-all.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
