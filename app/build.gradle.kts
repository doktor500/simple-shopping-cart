plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("io.arrow-kt:arrow-core:1.2.4")

    testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.20")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-framework-datatest:5.9.1")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(17))
    }
}