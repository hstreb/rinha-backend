FROM ubuntu:latest

# Install Oracle OpenJDK 21
RUN apt -y update && apt -y upgrade && apt -y install wget
RUN wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz
RUN tar zxvf jdk-21_linux-x64_bin.tar.gz
ENV JAVA_HOME=/jdk-21
ENV PATH="$JAVA_HOME/bin:${PATH}"

COPY build/libs/rinha-spring-virtual-threads-0.0.2.jar app.jar

CMD ["java", "-XX:MaxRAMPercentage=80.0", "-jar", "app.jar"]
