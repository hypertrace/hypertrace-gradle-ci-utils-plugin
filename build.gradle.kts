import org.hypertrace.gradle.publishing.License.APACHE_2_0

plugins {
  `java-gradle-plugin`
  id("org.hypertrace.ci-utils-plugin") version "0.1.1"
  id("org.hypertrace.publish-plugin") version "0.3.1"
}

group = "org.hypertrace.gradle.ci"

java {
  targetCompatibility = JavaVersion.VERSION_11
  sourceCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
  plugins {
    create("gradlePlugin") {
      id = "org.hypertrace.ci-utils-plugin"
      implementationClass = "org.hypertrace.gradle.ci.CiUtilsPlugin"
    }
  }
}

hypertracePublish {
  license.set(APACHE_2_0)
}