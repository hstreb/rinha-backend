FROM eclipse-temurin:20.0.2_9-jdk

COPY build/libs/rinha-spring-virtual-threads-0.0.1.jar app.jar

CMD ["java", "-XX:MaxRAMPercentage=80.0", "--enable-preview", "-jar", "app.jar"]
