plugins {
    id("java")
}

group = "org"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(project(":general"))
}


tasks.jar{
    manifest{
        attributes(
            mapOf(
                "Main-Class" to "org.Client"
            )
        )
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(
        configurations.runtimeClasspath.get().filter{ it.exists() }.map { if (it.isDirectory) it else zipTree(it)}
    )
}


tasks.test {
    useJUnitPlatform()
}