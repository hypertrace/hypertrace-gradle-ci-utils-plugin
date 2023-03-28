package org.hypertrace.gradle.ci.tasks;

import java.io.File;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.reporting.Reporting;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.options.Option;

public class CopyReportsTask extends DefaultTask {

  @Option(
      option = "output-dir",
      description = "Configures the output dir for the collected reports")
  public void setOutputDir(Object outputDir) {
    this.outputDir = this.getProject().mkdir(outputDir);
  }

  @Internal
  public File getOutputDir() {
    return this.outputDir;
  }

  @OutputDirectory
  public File getNamespacedOutputDir() {
    return new File(this.getOutputDir(), this.getProject().getName());
  }

  private File outputDir;

  public CopyReportsTask() {
    this.setOutputDir(new File(getProject().getBuildDir(), "collected-reports"));
  }

  @TaskAction
  public void copyFiles() {
    this.getReportingTasks(this.getProject())
        .getAsMap()
        .forEach(
            (reportTaskName, reportTask) -> {
              File targetDirectoryForReportTask =
                  new File(this.getNamespacedOutputDir(), reportTaskName);
              this.getProject()
                  .copy(
                      copySpec -> {
                        copySpec.from(reportTask);
                        copySpec.into(targetDirectoryForReportTask);
                      });
            });
  }

  private TaskCollection<Task> getReportingTasks(Project project) {
    return project
        .getTasks()
        .matching(
            task -> task instanceof Reporting && !((Reporting<?>) task).getReports().isEmpty());
  }
}
