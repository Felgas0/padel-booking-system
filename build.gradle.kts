plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.10"
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "org.postgresql", name = "postgresql", version = "42.+")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
    implementation(platform("org.http4k:http4k-bom:6.1.0.1"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-jetty")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.slf4j:slf4j-simple:2.0.9") // for logging
    implementation("org.springframework.security:spring-security-crypto:6.4.4")
    testImplementation(kotlin("test"))
}

ktlint {
    ignoreFailures.set(true)
}

tasks.register<Copy>("copyRuntimeDependencies") {
    into("build/libs")
    from(configurations.runtimeClasspath)
}

tasks.register<JavaExec>("launch") {
    group = "launch"
    description = "Run the application"
    mainClass.set("pt.isel.ls.AppLunchKt")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.named<Jar>("jar") {
    dependsOn("copyRuntimeDependencies")
    manifest {
        attributes["Main-Class"] = "pt.isel.ls.AppLunchKt"
        attributes["Class-Path"] = configurations.runtimeClasspath.get().joinToString(" ") { it.name }
    }
}
