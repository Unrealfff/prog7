plugins {
    id("java")
}

group = "org"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.opencsv:opencsv:5.9")
    implementation(project(":general"))
    implementation("org.postgresql:postgresql:42.7.7")
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "org.Server"
            )
        )
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(
        configurations.runtimeClasspath.get().filter { it.exists() }.map { if (it.isDirectory) it else zipTree(it) }
    )
}

tasks.test {
    useJUnitPlatform()
}