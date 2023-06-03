plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    java
}

apply { plugin("io.spring.dependency-management") }

group = "pl.mb.github-auth"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security:2.3.1.RELEASE")
    implementation("org.springframework.security:spring-security-config:5.3.4.RELEASE")
    implementation("org.springframework.security.oauth:spring-security-oauth2:2.5.2.RELEASE")
    implementation("org.springframework.security:spring-security-oauth2-client:5.3.4.RELEASE")
    implementation("org.apache.httpcomponents:httpclient:4.5")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}
