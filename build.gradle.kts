import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    kotlin("jvm") version "1.4.31" apply false
    kotlin("plugin.serialization") version "1.4.30" apply false

    `java-library`
    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
}

group = "com.github.omarmiatello.yeelight"
version = "1.0.3"

val p = loadProperties(file("local.properties").absolutePath)
p.stringPropertyNames().forEach { key ->
    val value = p.getProperty(key)
    ext[key] = value
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
val ossrhUsername: String by ext
val ossrhPassword: String by ext

nexusPublishing {
    repositories {
        create("myNexus") {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(ossrhUsername) // defaults to project.properties["myNexusUsername"]
            password.set(ossrhPassword) // defaults to project.properties["myNexusPassword"]
        }
    }
}