package org.endeavourhealth.imapi.model.config;

public class Metrics {
  private MetricsConsole console;
  private MetricsGraphite graphite;

  public MetricsConsole getConsole() {
    return console;
  }

  public Metrics setConsole(MetricsConsole console) {
    this.console = console;
    return this;
  }

  public MetricsGraphite getGraphite() {
    return graphite;
  }

  public Metrics setGraphite(MetricsGraphite graphite) {
    this.graphite = graphite;
    return this;
  }
}
