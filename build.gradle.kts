
plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.9.23"
}

repositories {
    mavenCentral()
}

val javaVersion = 21
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}
tasks.withType(JavaCompile::class) {
    options.release.set(javaVersion)
}
dependencies {
    implementation(group = "org.postgresql", name = "postgresql", version = "42.+")
    implementation(group = "org.http4k", name = "http4k-core", version = "5.18.2.0")
    implementation(group = "org.http4k", name = "http4k-server-jetty", version = "5.18.2.0")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version = "1.6.3")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-datetime", version = "0.6.0-RC")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "2.0.12")
    runtimeOnly(group = "org.slf4j", name = "slf4j-simple", version = "2.0.13")
    implementation(platform("org.http4k:http4k-bom:5.18.2.0"))
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-client-apache")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(kotlin("test"))

}

tasks.register<Jar>("2324-2-LEIC42D-G05") {
    manifest {
        attributes["Main-Class"] = "pt.isel.ls.server.SessionsServerKt"
    }
    from (
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    )
    from("gradle")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    with(tasks.jar.get())
}


kotlin {
    jvmToolchain(javaVersion)
}

tasks.register<Copy>("copyRuntimeDependencies") {
    into("build/libs")
    from(configurations.runtimeClasspath)
}

tasks.register<JavaExec>("launch") {
    group = "launch"
    this.mainClass.set("pt.isel.ls.server.SessionsServerKt")
    classpath = sourceSets.getByName("main").runtimeClasspath
}

tasks.test {
    useJUnitPlatform()

}