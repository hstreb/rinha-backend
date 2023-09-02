plugins {
  id("java")
  id("org.springframework.boot") version "3.1.3"
  id("io.spring.dependency-management") version "1.1.3"
  id("org.graalvm.buildtools.native") version "0.9.24"
}

group = "org.example"
version = "0.0.1"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  implementation("com.github.f4b6a3:uuid-creator:5.3.2")
  runtimeOnly("org.postgresql:r2dbc-postgresql")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
}

tasks.test {
  useJUnitPlatform()
}
