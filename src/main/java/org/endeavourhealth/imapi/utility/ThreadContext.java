package org.endeavourhealth.imapi.utility;

import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.List;

public class ThreadContext {
  public static final ThreadLocal<List<Graph>> userGraphs = new ThreadLocal<>();

  public static void setUserGraphs(List<Graph> graphs) {
    userGraphs.set(graphs);
  }
  public static List<Graph> getUserGraphs() {
    return userGraphs.get();
  }

  private ThreadContext() { }
}
