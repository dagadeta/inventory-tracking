plugins {
    alias(libs.plugins.flyway)
    alias(libs.plugins.kover)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.springframeworkBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinJpa)
}

group = "de.dagadeta"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val mockitoAgent by configurations.creating

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation(project(":common"))
    implementation(libs.kotlin.logging)

    runtimeOnly("org.postgresql:postgresql")

    developmentOnly("org.flywaydb:flyway-database-postgresql")
    developmentOnly("org.springframework.boot:spring-boot-starter-flyway")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    testImplementation(libs.zonky)
    testImplementation(libs.mockito.kotlin)
    testImplementation("org.mockito:mockito-core")

    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.flywaydb:flyway-database-postgresql")
    testRuntimeOnly("org.springframework.boot:spring-boot-starter-flyway")
    testRuntimeOnly("org.postgresql:postgresql")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        jvmArgs.add("-javaagent:${mockitoAgent.asPath}")
    }
    named<JavaExec>("bootRun") {
        workingDir = file(rootDir)
    }
}
