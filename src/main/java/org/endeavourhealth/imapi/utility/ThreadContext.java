package org.endeavourhealth.imapi.utility;

import org.endeavourhealth.imapi.vocabulary.GRAPH;

import java.util.List;

public class ThreadContext {
  public static final ThreadLocal<List<GRAPH>> userGraphs = new ThreadLocal<>();

  private ThreadContext() {
  }

  public static List<GRAPH> getUserGraphs() {
    if (userGraphs.get() == null)
      userGraphs.set(List.of(GRAPH.IM));

    return userGraphs.get();
  }

  public static void setUserGraphs(List<GRAPH> graphs) {
    userGraphs.set(graphs);
  }
}
