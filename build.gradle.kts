plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("org.jetbrains.kotlin.kapt") version "1.9.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.10"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.0.4"
    id("io.micronaut.aot") version "4.0.4"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
}

version = "0.1"
group = "com.tweet.uala"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    // Micronaut
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-retry")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.netty:netty-codec-http2:4.1.101.Final")
    implementation("jakarta.annotation:jakarta.annotation-api")

    // document
    implementation("io.micronaut.openapi:micronaut-openapi")
    implementation("io.swagger.core.v3:swagger-annotations")

    // db
    annotationProcessor("io.micronaut.data:micronaut-data-document-processor")
    implementation("io.micronaut.data:micronaut-data-mongodb")
    implementation("io.micronaut.mongodb:micronaut-mongo-sync")

    // Redis
    implementation("io.micronaut.redis:micronaut-redis-lettuce")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // test
    testImplementation("io.mockk:mockk:1.9.3")
}

application {
    mainClass = "com.tweet.uala.ApplicationKt"
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}
tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

kapt {
    arguments {
        arg("micronaut.openapi.views.spec", "swagger-ui.enabled=true,redoc.enabled=true")
    }
}

graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.tweet.uala.*")
    }
}
