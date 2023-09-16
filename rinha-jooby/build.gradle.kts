import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("application")
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

version = "0.0.1"
group = "org.exemplo"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

val joobyVersion = "3.0.5"

val mainClassName = "org.exemplo.rinha.App"

application {
  mainClass.set(mainClassName)
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation(platform("io.jooby:jooby-undertow:$joobyVersion"))
  implementation("io.jooby:jooby-undertow")
  implementation("io.jooby:jooby-logback")
  implementation("io.jooby:jooby-avaje-jsonb")
  implementation("io.jooby:jooby-hikari")
  implementation("com.github.f4b6a3:uuid-creator:5.3.2")

  runtimeOnly("org.postgresql:postgresql:42.6.0")

  annotationProcessor("io.avaje:avaje-jsonb-generator:1.7")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
  testImplementation("io.jooby:jooby-test")
  testImplementation("com.squareup.okhttp3:okhttp:4.11.0")
}

tasks.withType<ShadowJar> {
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
}
