import org.hypertrace.gradle.publishing.License.APACHE_2_0

plugins {
  `java-gradle-plugin`
  id("org.hypertrace.ci-utils-plugin") version "0.2.0"
  id("org.hypertrace.publish-plugin") version "1.0.2"
}

group = "org.hypertrace.gradle.ci"

java {
  targetCompatibility = JavaVersion.VERSION_1_8
  sourceCompatibility = JavaVersion.VERSION_1_8
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