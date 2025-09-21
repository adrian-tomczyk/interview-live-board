plugins {
    id("java")
}

group = "com.tomczyk.live-board"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.+")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.mockito:mockito-junit-jupiter:5.+")
    testImplementation("org.mockito:mockito-core:5.+")
}

tasks.test {
    useJUnitPlatform()
}