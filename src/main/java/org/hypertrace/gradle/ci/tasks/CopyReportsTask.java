package org.hypertrace.gradle.ci.tasks;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.reporting.Report;
import org.gradle.api.reporting.ReportContainer;
import org.gradle.api.reporting.Reporting;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
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
    this.getReportsForProject(this.getProject())
        .forEach(
            report -> {
              File targetDirectoryForReport =
                  new File(this.getNamespacedOutputDir(), report.getName());
              this.getProject()
                  .copy(
                      copySpec -> {
                        copySpec.from(report.getOutputLocation());
                        copySpec.into(targetDirectoryForReport);
                      });
            });
  }

  private Collection<Report> getReportsForProject(Project project) {
    return project.getTasks().matching(task -> task instanceof Reporting).stream()
        .map(this::castTaskToReporter)
        .flatMap(reporter -> reporter.getReports().stream())
        .collect(Collectors.toSet());
  }

  @SuppressWarnings("unchecked")
  private Reporting<ReportContainer<Report>> castTaskToReporter(Task task) {
    return (Reporting<ReportContainer<Report>>) task;
  }
}
