package org.hypertrace.gradle.ci;

import javax.annotation.Nonnull;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.hypertrace.gradle.ci.tasks.CopyReportsTask;

public class CiUtilsPlugin implements Plugin<Project> {
  public static final String COPY_ALL_REPORTS_TASK_NAME = "copyAllReports";
  public static final String DOWNLOAD_DEPENDENCIES_TASK_NAME = "downloadDependencies";
  public static final String PRINT_PROJECT_NAME = "printProjectName";
  private static final String TASK_GROUP = "CircleCI Utility";

  @Override
  public void apply(@Nonnull Project target) {
    target.allprojects(
        project -> {
          this.addReportCopyTaskToProject(project);
          this.addDownloadDependenciesTaskToProject(project);
          this.addPrintProjectNameTaskToProject(project);
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
}
