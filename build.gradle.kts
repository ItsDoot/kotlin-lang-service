plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "pw.dotdash"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kodein-framework/Kodein-DI/")
    maven("https://repo-new.spongepowered.org/repository/maven-public/")
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // plugin-spi and its dependencies
    implementation("org.spongepowered:plugin-spi:0.1.1-SNAPSHOT")
    implementation("com.google.inject:guice:4.0")

    implementation("org.kodein.di:kodein-di:7.0.0")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
}