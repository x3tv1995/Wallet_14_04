plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.lesson"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // https://mvnrepository.com/artifact/org.liquibase/liquibase-core
    implementation("org.liquibase:liquibase-core:4.31.1")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.4")
    // https://mvnrepository.com/artifact/org.testcontainers/junit-jupiter
    testImplementation("org.testcontainers:postgresql:1.20.6")
    testImplementation("org.testcontainers:junit-jupiter:1.20.6")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
