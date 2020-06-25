# Hypertrace CI Utils Plugin
###### org.hypertrace.ci-utils-plugin
[![CircleCI](https://circleci.com/gh/hypertrace/hypertrace-gradle-ci-utils-plugin.svg?style=svg)](https://circleci.com/gh/hypertrace/hypertrace-gradle-ci-utils-plugin)

### Purpose
This plugin is applied to a project to provide useful utility functions often used in CI jobs.

### Tasks

#### copyAllReports
Copies the reports of this project to the provided directory. They will
be named with the pattern `$output-dir/$project-name/$report-name/<contents>`. Note this task does not
have any dependencies, and assumes the reports themselves have already been generated.

Example usage:
```
./gradlew copyAllReports --output-dir=/tmp/report-dir
```

#### downloadDependencies
Forces the resolution of all dependencies for each resolvable configuration of this project. This
is useful in CI to populate a cache for reuse between jobs.
Example usage:
```
./gradlew downloadDependencies
```