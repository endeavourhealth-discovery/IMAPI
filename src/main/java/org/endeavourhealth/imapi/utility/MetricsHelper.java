package org.endeavourhealth.imapi.utility;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.model.config.Metrics;
import org.endeavourhealth.imapi.model.config.MetricsConsole;
import org.endeavourhealth.imapi.model.config.MetricsGraphite;
import org.endeavourhealth.imapi.vocabulary.CONFIG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MetricsHelper {

  //report metrics to graphite every minute
  private static final int GRAPHITE_REPORT_FREQUENCY = 1;
  private static final TimeUnit GRAPHITE_REPORT_UNITS = TimeUnit.MINUTES;

  private final MetricRegistry registry;

  private final Map<String, AtomicInteger> eventMap = new ConcurrentHashMap<>();

  private MetricsHelper() {
    ConfigManager configManager = new ConfigManager();
    this.registry = new MetricRegistry();

    try {
      Metrics config = configManager.getConfig(CONFIG.MONITORING, new TypeReference<>() {
      });
      if (config != null) {

        //set any console logging config
        initConsoleLogging(config);

        //set any graphite logging config
        initGraphiteLogging(config);

      } else {
        log.info("No metrics config record found");
      }

    } catch (Exception ex) {
      log.error("Error loading graphite config record", ex);
    }
  }

  private static MetricsHelper instance() {
    return InstanceHolder.instance;
  }

  public static void recordValue(String metric, long value) {
    instance().recordValueImpl(metric, value);
  }

  public static void recordEvent(String metric) {
    recordEvents(metric, 1);
  }

  public static void recordEvents(String metric, int num) {
    instance().recordEventImpl(metric, num);
  }

  public static MetricsTimer recordTime(String metric) {
    return instance().recordTimeImpl(metric);
  }

  public static Counter recordCounter(String metric) {
    return instance().recordCounterImpl(metric);
  }

  private static String getHostName() throws IOException {
    Runtime r = Runtime.getRuntime();
    Process p = r.exec(new String[] { "hostname" });
    try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
      return br.readLine();
    }
  }

  private void initConsoleLogging(Metrics config) {
    MetricsConsole con = config.getConsole();
    if (con != null) {

      int frequency = con.getFrequency();

      ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
      reporter.start(frequency, TimeUnit.SECONDS);
      log.info("Console metrics reporter started");
    }
  }

  private void initGraphiteLogging(Metrics config) throws IOException {
    MetricsGraphite grp = config.getGraphite();
    if (grp != null) {
      String address = grp.getAddress();
      int port = grp.getPort();

      String prefix = getHostName() + ".IMAPI";

      Graphite graphite = new Graphite(new InetSocketAddress(address, port));

      //the below variables came from https://metrics.dropwizard.io/4.0.0/manual/graphite.html#manual-graphite
      GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
        .prefixedWith(prefix)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(graphite);

      //set up default metrics for whatever app we're running
      registry.register("Garbage Collection", new GarbageCollectorMetricSet());
      registry.register("Buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
      registry.register("Memory", new MemoryUsageGaugeSet());
      registry.register("Threads", new ThreadStatesGaugeSet());
      registry.register("File Descriptor", new FileDescriptorRatioGauge());

      //send metrics every minute
      reporter.start(GRAPHITE_REPORT_FREQUENCY, GRAPHITE_REPORT_UNITS);

      log.info("Graphite metrics reporter started [{}]", prefix);
    }
  }

/*    public static MetricRegistry getRegistry() {
        return instance().registry;
    }

    public static void startHeartbeat() {
        instance().startHeartbeatImpl();
    }

    private void startHeartbeatImpl() {
        //remove any existing one, just in case this is called twice
        registry.remove("heartbeat");

        HeartbeatGaugeImpl gauge = new HeartbeatGaugeImpl();
        registry.register("heartbeat", gauge);
    }
*/

  private void recordValueImpl(String metric, long value) {
    Histogram histogram = registry.histogram(metric);
    histogram.update(value);
  }

  private void recordEventImpl(String metric, int num) {
    AtomicInteger val = eventMap.get(metric);
    if (val == null) {
      //if null, sync and make a second check so we're sure we're not registering the gauge twice
      synchronized (eventMap) {
        val = eventMap.get(metric);
        if (val == null) {
          val = new AtomicInteger(0);
          eventMap.put(metric, val);

          EventGaugeImpl gauge = new EventGaugeImpl(metric);
          registry.register(metric, gauge);
        }
      }
    }

    //finally increment the value
    val.addAndGet(num);
  }

  private MetricsTimer recordTimeImpl(String metric) {
    Timer timer = registry.timer(metric);
    return new MetricsTimer(timer.time());
  }

  private Counter recordCounterImpl(String metric) {
    return registry.counter(metric);
  }

  private static final class InstanceHolder {
    private static final MetricsHelper instance = new MetricsHelper();
  }

  /**
   * Gauge for logging discrete events that happen over time. As the recordEvent(..)
   * function is called, an AtomicInteger is incremented. When this gauge is polled for its
   * value, the current value is returned and the int set back to zero.
   */
  class EventGaugeImpl extends CachedGauge<Integer> {

    private final String name;

    public EventGaugeImpl(String name) {
      //cache the value for the same period of time as the reporting to graphite
      super(GRAPHITE_REPORT_FREQUENCY, GRAPHITE_REPORT_UNITS);
      this.name = name;
    }

    @Override
    protected Integer loadValue() {
      AtomicInteger val = eventMap.get(name);
      if (val == null) {
        return 0;
      } else {
        return val.getAndSet(0);
      }
    }
  }

  /*    *//**
   * simple gauge that just reports a value of 1 whenever polled, to report the application is running
   *//*
    class HeartbeatGaugeImpl implements Gauge<Integer> {

        public HeartbeatGaugeImpl() {}

        @Override
        public Integer getValue() {
            return 1;
        }
    }*/
}
