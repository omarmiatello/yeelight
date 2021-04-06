import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    kotlin("jvm") version "1.4.31" apply false
    kotlin("plugin.serialization") version "1.4.30" apply false

//    `java-library`
//    `maven-publish`
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
}

val p = loadProperties(file("local.properties").absolutePath)
p.stringPropertyNames().forEach { key ->
    val value = p.getProperty(key)
    ext[key] = value
}
val ossrhUsername: String by ext
val ossrhPassword: String by ext
val signingKeyId: String by ext
val signingPassword: String by ext
val signingSecretKeyRingFile: String by ext

ext["signing.keyId"] = signingKeyId
ext["signing.password"] = signingPassword
ext["signing.secretKeyRingFile"] = signingSecretKeyRingFile

//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            from(components["java"])
//        }
//    }
//}

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

group = "com.github.omarmiatello.yeelight"
version = "1.0.3"

configure(subprojects
        - project(":app")) {
    apply<JavaLibraryPlugin>()
    apply<JacocoPlugin>()
    apply<SigningPlugin>()
    apply<MavenPublishPlugin>()

    configure<JavaPluginExtension> {
        withJavadocJar()
        withSourcesJar()
    }

    configure<SigningExtension> {
        val publishing: PublishingExtension by project

        //useInMemoryPgpKeys(signingKeyId, signingPassword)
        sign(publishing.publications)
    }

    configure<PublishingExtension> {
        publications {
            val main by creating(MavenPublication::class) {
                from(components["java"])

                pom {
                    name.set("yeelight :: ${project.name}")
                    description.set("yeelight :: ${project.name} :: Control your Xiaomi Yeelight lamp using Kotlin")
                    url.set("https://github.com/omarmiatello/yeelight")
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("jackl85")
                            name.set("Omar Miatello")
                            email.set("omar.miatello@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/omarmiatello/yeelight.git")
                        developerConnection.set("scm:git:ssh://github.com/omarmiatello/yeelight.git")
                        url.set("https://github.com/omarmiatello/yeelight/tree/master")
                    }
                }
            }
        }
        repositories {
            maven {
                name = project.name
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }
}
