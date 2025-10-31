package org.endeavourhealth.imapi.transformation.cli;

import java.util.*;

/**
 * Enhanced configuration class for QOF to IMQ transformation.
 * 
 * Supports:
 * - YAML and JSON configuration files
 * - Environment variable overrides
 * - Command-line parameter overrides
 * - Configuration profiles (dev, prod, test)
 * - Default values and validation
 * 
 * Configuration precedence (highest to lowest):
 * 1. Command-line parameters
 * 2. Environment variables
 * 3. Configuration file values
 * 4. Profile-specific defaults
 * 5. Global defaults
 */
public class TransformationConfiguration {

    public enum Profile {
        DEV("dev"),
        PROD("prod"),
        TEST("test");

        private final String name;

        Profile(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Profile fromString(String name) {
            for (Profile p : Profile.values()) {
                if (p.name.equalsIgnoreCase(name)) {
                    return p;
                }
            }
            throw new IllegalArgumentException("Unknown profile: " + name);
        }
    }

    // Core transformation parameters
    private String inputPath;
    private String outputPath;
    private boolean batchMode = false;
    private String filePattern = "*.json";
    private boolean verbose = false;
    private boolean parallelProcessing = false;
    private String configurationFile;

    // Performance and resource settings
    private int maxParallelThreads = Runtime.getRuntime().availableProcessors();
    private int cacheSize = 10000;
    private int batchSize = 100;
    private long maxMemoryUsage = Runtime.getRuntime().maxMemory();

    // Error handling
    private boolean continueOnError = true;
    private int maxRetries = 3;
    private long retryDelayMs = 1000;

    // Output options
    private boolean overwriteOutput = false;
    private boolean createBackup = true;
    private String outputEncoding = "UTF-8";

    // Logging and monitoring
    private String logLevel = "INFO";
    private boolean enableMetrics = false;
    private String metricsOutput;

    // Profile and defaults
    private Profile profile = Profile.PROD;
    private Map<String, String> customProperties = new HashMap<>();

    // Validation state
    private List<String> validationErrors = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();

    // ===== Static Defaults =====

    public static final class Defaults {
        // Transformation defaults
        public static final String FILE_PATTERN = "*.json";
        public static final boolean BATCH_MODE = false;
        public static final boolean VERBOSE = false;
        public static final boolean PARALLEL_PROCESSING = false;

        // Performance defaults
        public static final int MAX_PARALLEL_THREADS = Runtime.getRuntime().availableProcessors();
        public static final int CACHE_SIZE = 10000;
        public static final int BATCH_SIZE = 100;

        // Error handling defaults
        public static final boolean CONTINUE_ON_ERROR = true;
        public static final int MAX_RETRIES = 3;
        public static final long RETRY_DELAY_MS = 1000;

        // Output defaults
        public static final boolean OVERWRITE_OUTPUT = false;
        public static final boolean CREATE_BACKUP = true;
        public static final String OUTPUT_ENCODING = "UTF-8";

        // Logging defaults
        public static final String LOG_LEVEL = "INFO";
        public static final boolean ENABLE_METRICS = false;
    }

    // ===== Profile-Specific Defaults =====

    public static class ProfileDefaults {
        private static final Map<Profile, ConfigProfile> profileMap = new HashMap<>();

        static {
            // Development profile - verbose, single-threaded
            profileMap.put(Profile.DEV, new ConfigProfile(
                    "DEBUG",      // logLevel
                    true,         // enableMetrics
                    1,            // maxParallelThreads
                    false,        // parallelProcessing
                    true,         // continueOnError
                    false         // overwriteOutput
            ));

            // Production profile - optimized for performance
            profileMap.put(Profile.PROD, new ConfigProfile(
                    "WARN",       // logLevel
                    false,        // enableMetrics
                    Runtime.getRuntime().availableProcessors(),  // maxParallelThreads
                    true,         // parallelProcessing
                    true,         // continueOnError
                    false         // overwriteOutput
            ));

            // Test profile - isolated, no side effects
            profileMap.put(Profile.TEST, new ConfigProfile(
                    "INFO",       // logLevel
                    false,        // enableMetrics
                    1,            // maxParallelThreads
                    false,        // parallelProcessing
                    false,        // continueOnError (fail fast in tests)
                    true          // overwriteOutput (clean state)
            ));
        }

        public static ConfigProfile getProfileDefaults(Profile profile) {
            return profileMap.getOrDefault(profile, profileMap.get(Profile.PROD));
        }

        public static class ConfigProfile {
            public final String logLevel;
            public final boolean enableMetrics;
            public final int maxParallelThreads;
            public final boolean parallelProcessing;
            public final boolean continueOnError;
            public final boolean overwriteOutput;

            public ConfigProfile(String logLevel, boolean enableMetrics, int maxParallelThreads,
                               boolean parallelProcessing, boolean continueOnError, 
                               boolean overwriteOutput) {
                this.logLevel = logLevel;
                this.enableMetrics = enableMetrics;
                this.maxParallelThreads = maxParallelThreads;
                this.parallelProcessing = parallelProcessing;
                this.continueOnError = continueOnError;
                this.overwriteOutput = overwriteOutput;
            }
        }
    }

    // ===== Constructors =====

    public TransformationConfiguration() {
        applyProfileDefaults(Profile.PROD);
    }

    public TransformationConfiguration(Profile profile) {
        this.profile = profile;
        applyProfileDefaults(profile);
    }

    // ===== Profile Management =====

    public void applyProfileDefaults(Profile profile) {
        this.profile = profile;
        ProfileDefaults.ConfigProfile defaults = ProfileDefaults.getProfileDefaults(profile);
        
        this.logLevel = defaults.logLevel;
        this.enableMetrics = defaults.enableMetrics;
        this.maxParallelThreads = defaults.maxParallelThreads;
        this.parallelProcessing = defaults.parallelProcessing;
        this.continueOnError = defaults.continueOnError;
        this.overwriteOutput = defaults.overwriteOutput;
    }

    // ===== Environment Variable Override =====

    public void applyEnvironmentVariables() {
        if (System.getenv("QOF_INPUT_PATH") != null) {
            this.inputPath = System.getenv("QOF_INPUT_PATH");
        }
        if (System.getenv("QOF_OUTPUT_PATH") != null) {
            this.outputPath = System.getenv("QOF_OUTPUT_PATH");
        }
        if (System.getenv("QOF_BATCH_MODE") != null) {
            this.batchMode = Boolean.parseBoolean(System.getenv("QOF_BATCH_MODE"));
        }
        if (System.getenv("QOF_FILE_PATTERN") != null) {
            this.filePattern = System.getenv("QOF_FILE_PATTERN");
        }
        if (System.getenv("QOF_VERBOSE") != null) {
            this.verbose = Boolean.parseBoolean(System.getenv("QOF_VERBOSE"));
        }
        if (System.getenv("QOF_PARALLEL") != null) {
            this.parallelProcessing = Boolean.parseBoolean(System.getenv("QOF_PARALLEL"));
        }
        if (System.getenv("QOF_MAX_THREADS") != null) {
            try {
                this.maxParallelThreads = Integer.parseInt(System.getenv("QOF_MAX_THREADS"));
            } catch (NumberFormatException e) {
                addWarning("Invalid QOF_MAX_THREADS value: " + System.getenv("QOF_MAX_THREADS"));
            }
        }
        if (System.getenv("QOF_CACHE_SIZE") != null) {
            try {
                this.cacheSize = Integer.parseInt(System.getenv("QOF_CACHE_SIZE"));
            } catch (NumberFormatException e) {
                addWarning("Invalid QOF_CACHE_SIZE value: " + System.getenv("QOF_CACHE_SIZE"));
            }
        }
        if (System.getenv("QOF_LOG_LEVEL") != null) {
            this.logLevel = System.getenv("QOF_LOG_LEVEL");
        }
        if (System.getenv("QOF_PROFILE") != null) {
            try {
                applyProfileDefaults(Profile.fromString(System.getenv("QOF_PROFILE")));
            } catch (IllegalArgumentException e) {
                addWarning("Invalid QOF_PROFILE value: " + System.getenv("QOF_PROFILE"));
            }
        }
    }

    // ===== Validation =====

    public boolean validate() {
        validationErrors.clear();

        // Input path is required
        if (inputPath == null || inputPath.trim().isEmpty()) {
            addError("Input path is required (--input parameter or QOF_INPUT_PATH env var)");
        }

        // Output path is required
        if (outputPath == null || outputPath.trim().isEmpty()) {
            addError("Output path is required (--output parameter or QOF_OUTPUT_PATH env var)");
        }

        // Validate thread count
        if (maxParallelThreads < 1) {
            addError("Max parallel threads must be >= 1");
        }

        // Validate cache size
        if (cacheSize < 0) {
            addError("Cache size must be >= 0");
        }

        // Validate batch size
        if (batchSize < 1) {
            addError("Batch size must be >= 1");
        }

        // Validate max retries
        if (maxRetries < 0) {
            addError("Max retries must be >= 0");
        }

        // Validate retry delay
        if (retryDelayMs < 0) {
            addError("Retry delay must be >= 0");
        }

        // File pattern validation
        if (filePattern == null || filePattern.trim().isEmpty()) {
            addError("File pattern cannot be empty");
        }

        // Log level validation
        if (!isValidLogLevel(logLevel)) {
            addWarning("Unknown log level: " + logLevel + ". Using INFO");
            this.logLevel = "INFO";
        }

        // Encoding validation
        if (!isValidEncoding(outputEncoding)) {
            addWarning("Unknown encoding: " + outputEncoding + ". Using UTF-8");
            this.outputEncoding = "UTF-8";
        }

        return validationErrors.isEmpty();
    }

    private boolean isValidLogLevel(String level) {
        if (level == null) return false;
        String[] validLevels = {"TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF"};
        for (String valid : validLevels) {
            if (valid.equalsIgnoreCase(level)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidEncoding(String encoding) {
        try {
            java.nio.charset.Charset.forName(encoding);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ===== Error/Warning Management =====

    public void addError(String error) {
        validationErrors.add(error);
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public boolean isValid() {
        return validationErrors.isEmpty();
    }

    public List<String> getValidationErrors() {
        return new ArrayList<>(validationErrors);
    }

    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }

    // ===== Merge from CliConfiguration =====

    public void mergeFromCliConfiguration(CliConfiguration cliConfig) {
        if (cliConfig.getInputPath() != null) {
            this.inputPath = cliConfig.getInputPath();
        }
        if (cliConfig.getOutputPath() != null) {
            this.outputPath = cliConfig.getOutputPath();
        }
        if (cliConfig.isBatchMode()) {
            this.batchMode = true;
        }
        if (!cliConfig.getFilePattern().equals(Defaults.FILE_PATTERN)) {
            this.filePattern = cliConfig.getFilePattern();
        }
        if (cliConfig.isVerbose()) {
            this.verbose = true;
        }
        if (cliConfig.isParallelProcessing()) {
            this.parallelProcessing = true;
        }
    }

    // ===== Getters and Setters =====

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean isBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = Objects.requireNonNull(filePattern, "File pattern cannot be null");
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isParallelProcessing() {
        return parallelProcessing;
    }

    public void setParallelProcessing(boolean parallelProcessing) {
        this.parallelProcessing = parallelProcessing;
    }

    public String getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
    }

    public int getMaxParallelThreads() {
        return maxParallelThreads;
    }

    public void setMaxParallelThreads(int maxParallelThreads) {
        this.maxParallelThreads = maxParallelThreads;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public long getMaxMemoryUsage() {
        return maxMemoryUsage;
    }

    public void setMaxMemoryUsage(long maxMemoryUsage) {
        this.maxMemoryUsage = maxMemoryUsage;
    }

    public boolean isContinueOnError() {
        return continueOnError;
    }

    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getRetryDelayMs() {
        return retryDelayMs;
    }

    public void setRetryDelayMs(long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }

    public boolean isOverwriteOutput() {
        return overwriteOutput;
    }

    public void setOverwriteOutput(boolean overwriteOutput) {
        this.overwriteOutput = overwriteOutput;
    }

    public boolean isCreateBackup() {
        return createBackup;
    }

    public void setCreateBackup(boolean createBackup) {
        this.createBackup = createBackup;
    }

    public String getOutputEncoding() {
        return outputEncoding;
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    public String getMetricsOutput() {
        return metricsOutput;
    }

    public void setMetricsOutput(String metricsOutput) {
        this.metricsOutput = metricsOutput;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        applyProfileDefaults(profile);
    }

    public Map<String, String> getCustomProperties() {
        return new HashMap<>(customProperties);
    }

    public void setCustomProperty(String key, String value) {
        customProperties.put(key, value);
    }

    public String getCustomProperty(String key) {
        return customProperties.get(key);
    }

    // ===== Builder Pattern =====

    public static class Builder {
        private final TransformationConfiguration config;

        public Builder() {
            this.config = new TransformationConfiguration();
        }

        public Builder withProfile(Profile profile) {
            config.applyProfileDefaults(profile);
            return this;
        }

        public Builder withInputPath(String inputPath) {
            config.setInputPath(inputPath);
            return this;
        }

        public Builder withOutputPath(String outputPath) {
            config.setOutputPath(outputPath);
            return this;
        }

        public Builder withBatchMode(boolean batchMode) {
            config.setBatchMode(batchMode);
            return this;
        }

        public Builder withFilePattern(String filePattern) {
            config.setFilePattern(filePattern);
            return this;
        }

        public Builder withVerbose(boolean verbose) {
            config.setVerbose(verbose);
            return this;
        }

        public Builder withParallelProcessing(boolean parallel) {
            config.setParallelProcessing(parallel);
            return this;
        }

        public Builder withMaxParallelThreads(int threads) {
            config.setMaxParallelThreads(threads);
            return this;
        }

        public Builder withCacheSize(int size) {
            config.setCacheSize(size);
            return this;
        }

        public Builder withContinueOnError(boolean continueOnError) {
            config.setContinueOnError(continueOnError);
            return this;
        }

        public Builder withLogLevel(String logLevel) {
            config.setLogLevel(logLevel);
            return this;
        }

        public Builder withEnvironmentVariables() {
            config.applyEnvironmentVariables();
            return this;
        }

        public TransformationConfiguration build() {
            return config;
        }
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "TransformationConfiguration{" +
                "profile=" + profile +
                ", inputPath='" + inputPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", batchMode=" + batchMode +
                ", filePattern='" + filePattern + '\'' +
                ", verbose=" + verbose +
                ", parallelProcessing=" + parallelProcessing +
                ", maxParallelThreads=" + maxParallelThreads +
                ", cacheSize=" + cacheSize +
                ", continueOnError=" + continueOnError +
                ", logLevel='" + logLevel + '\'' +
                '}';
    }
}