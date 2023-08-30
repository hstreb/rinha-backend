plugins {
    id("java")
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.graalvm.buildtools.native") version "0.9.24"
}

group = "org.example"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_20
    targetCompatibility = JavaVersion.VERSION_20
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("com.github.f4b6a3:uuid-creator:5.3.2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    val preview = "--enable-preview"
    withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        jvmArgs = mutableListOf(preview)
    }
    withType<JavaExec> {
        jvmArgs = mutableListOf(preview)
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add(preview)
        options.compilerArgs.add("-Xlint:preview")
    }
}
