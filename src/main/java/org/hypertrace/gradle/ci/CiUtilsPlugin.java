package org.hypertrace.gradle.ci;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.hypertrace.gradle.ci.tasks.CopyReportsTask;

import javax.annotation.Nonnull;
import java.io.File;

public class CiUtilsPlugin implements Plugin<Project> {
  public static final String COPY_ALL_REPORTS_TASK_NAME = "copyAllReports";
  public static final String DOWNLOAD_DEPENDENCIES_TASK_NAME = "downloadDependencies";
  public static final String PRINT_PROJECT_NAME = "printProjectName";
  public static final String COPY_DEPENDENCIES_TASK_NAME = "copyDependencies";
  private static final String TASK_GROUP = "CircleCI Utility";

  @Override
  public void apply(@Nonnull Project target) {
    target.allprojects(
        project -> {
          this.addReportCopyTaskToProject(project);
          this.addDownloadDependenciesTaskToProject(project);
          this.addPrintProjectNameTaskToProject(project);
          this.addCopyDependenciesTaskToProject(project);
        });
  }

  private void addReportCopyTaskToProject(Project project) {
    project
        .getTasks()
        .register(
            COPY_ALL_REPORTS_TASK_NAME,
            CopyReportsTask.class,
            task -> {
              task.setGroup(TASK_GROUP);
              task.setDescription(
                  "Copies reports of this project and all children to specified location");
            });
  }

  private void addDownloadDependenciesTaskToProject(Project project) {
    // Create a task to download all dependencies for CI caching
    project
        .getTasks()
        .register(
            DOWNLOAD_DEPENDENCIES_TASK_NAME,
            createdTask -> {
              createdTask.setDescription("Download all dependencies to the Gradle cache");
              createdTask.doLast(
                  unused ->
                      project
                          .getConfigurations()
                          .matching(Configuration::isCanBeResolved)
                          .forEach(Configuration::resolve));
            });
  }

  private void addPrintProjectNameTaskToProject(Project project) {
    project
        .getTasks()
        .register(
            PRINT_PROJECT_NAME,
            createdTask -> {
              createdTask.setDescription("Outputs the project name");
              createdTask.doLast(unused -> project.getLogger().quiet(project.getName()));
            });
  }

  private void addCopyDependenciesTaskToProject(Project project) {
    // Create a task to copy all dependencies to build/dependencies folder
    project
      .getTasks()
      .register(
        COPY_DEPENDENCIES_TASK_NAME,
        createdTask -> {
          createdTask.setDescription("Copy dependencies of a project to build/dependencies folder");
          createdTask.doLast(unused -> {
            File destination = new File(project.getBuildDir(), "dependencies");
            project.mkdir(destination);
            Configuration configuration = project.getConfigurations().findByName("runtimeClasspath");
            if (configuration != null) {
              configuration
                .resolve()
                .stream()
                .filter(file -> file.getName().endsWith(".jar"))
                .forEach(file -> {
                  project.copy(copySpec -> {
                    copySpec.from(file);
                    copySpec.into(destination);
                  });
                });
            }
          });
        });
  }
}
