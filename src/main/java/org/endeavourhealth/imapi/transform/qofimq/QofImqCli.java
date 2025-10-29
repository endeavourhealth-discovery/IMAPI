package org.endeavourhealth.imapi.transform.qofimq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static org.endeavourhealth.imapi.transform.qofimq.QofBatchModels.Summary;

/**
 * Simple CLI entry point gated behind the presence of the flag "--qofimq-cli".
 *
 * Usage examples:
 *   java -jar app.jar --qofimq-cli --input="Z:\\Data\\QOF" --output="Z:\\Data\\QOF\\_imq_out" --emit-json --fail-fast
 */
@Component
public class QofImqCli implements ApplicationRunner {
  private static final Logger LOG = LoggerFactory.getLogger(QofImqCli.class);

  private final QofImqProperties props;
  private final QofBatchProcessor batch;

  public QofImqCli(QofImqProperties props, QofBatchProcessor batch) {
    this.props = props;
    this.batch = batch;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (!args.containsOption("qofimq-cli")) {
      return; // Not in CLI mode, do nothing.
    }

    // Override properties from CLI flags if provided
    if (args.containsOption("input")) {
      String val = args.getOptionValues("input").get(0);
      props.setInputPath(val);
    }
    if (args.containsOption("output")) {
      String val = args.getOptionValues("output").get(0);
      props.setOutputPath(val);
    }
    if (args.containsOption("emit-json")) {
      props.setEmitJson(true);
    }
    boolean failFast = args.containsOption("fail-fast");

    int exitCode = 0;
    try {
      Summary s = batch.runBatch(failFast);
      // Exit code: 0 on success, otherwise number of failures (capped to 255)
      exitCode = (s.failure == 0) ? 0 : Math.min(255, s.failure);
    } catch (Exception ex) {
      LOG.error("Unexpected error in QOF→IMQ CLI: {}", ex.getMessage(), ex);
      exitCode = 2; // Non-zero for unexpected exception
    }

    // Terminate the JVM with the computed exit code as per requirement
    System.exit(exitCode);
  }
}
