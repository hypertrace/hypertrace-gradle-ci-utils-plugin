pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    maven {
      url = uri("https://us-maven.pkg.dev/hypertrace-repos/maven")
    }
  }
}

plugins {
  id("org.hypertrace.version-settings") version "0.3.0"
}

rootProject.name = "hypertrace-gradle-ci-utils-plugin"
