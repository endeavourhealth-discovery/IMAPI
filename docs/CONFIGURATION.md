# Configuration Management Guide

This document describes the complete configuration system for the QOF to IMQ transformation CLI and API.

## Table of Contents

- [Overview](#overview)
- [Configuration Files](#configuration-files)
- [Configuration Precedence](#configuration-precedence)
- [Configuration Options](#configuration-options)
- [Profiles](#profiles)
- [Environment Variables](#environment-variables)
- [Examples](#examples)

## Overview

The QOF to IMQ transformation system provides flexible configuration through multiple mechanisms:

1. **Configuration Files** - YAML or JSON format
2. **Command-Line Arguments** - Override file configuration
3. **Environment Variables** - System-level overrides
4. **Profiles** - Pre-defined settings for dev/prod/test
5. **Defaults** - Built-in fallback values

Configuration is composed of several sections:

- **Transformation** - Core transformation parameters
- **Performance** - Resource and tuning settings
- **Error Handling** - Error resilience configuration
- **Output** - Output file handling
- **Logging** - Log level and output
- **Monitoring** - Metrics and observability

## Configuration Files

### Supported Formats

#### YAML Format (Recommended)
```yaml
# config.yaml
inputPath: /data/input.json
outputPath: /data/output.json
batch: true
profile: prod
```

#### JSON Format
```json
{
  "inputPath": "/data/input.json",
  "outputPath": "/data/output.json",
  "batch": true,
  "profile": "prod"
}
```

### File Location and Discovery

Configuration files can be specified using the `--config` command-line argument:

```bash
java -jar qofimq-cli.jar --config=/etc/qofimq/config.yaml
```

### Profile-Specific Configuration

You can create profile-specific configuration files that override the base configuration:

```
config.yaml              # Base configuration
config-dev.yaml          # Development profile
config-prod.yaml         # Production profile
config-test.yaml         # Test profile
```

When loading configuration with a profile, the loader will automatically look for a profile-specific file:

```bash
# Loads config-prod.yaml if it exists, otherwise falls back to config.yaml
java -jar qofimq-cli.jar --config=config.yaml --profile=prod
```

## Configuration Precedence

Configuration values are applied in the following order (highest to lowest priority):

1. **Command-Line Arguments** - Highest priority
   ```bash
   java -jar qofimq-cli.jar --input=/data/input.json
   ```

2. **Environment Variables** - Override file configuration
   ```bash
   export QOF_INPUT_PATH=/data/input.json
   java -jar qofimq-cli.jar
   ```

3. **Configuration File** - User-specified values
   ```bash
   java -jar qofimq-cli.jar --config=config.yaml
   ```

4. **Profile Defaults** - Profile-specific defaults
   ```bash
   java -jar qofimq-cli.jar --profile=dev
   ```

5. **Global Defaults** - Lowest priority
   Built-in defaults for all settings

### Example: Precedence in Action

```bash
# In config.yaml:
# inputPath: /config/input.json
# maxParallelThreads: 4

# Export environment variable:
export QOF_INPUT_PATH=/env/input.json
export QOF_MAX_THREADS=8

# Run with command-line override:
java -jar qofimq-cli.jar \
  --config=config.yaml \
  --input=/cli/input.json \
  --max-threads=16

# Final configuration:
# inputPath: /cli/input.json (from CLI)
# maxParallelThreads: 16 (from CLI)
```

## Configuration Options

### Transformation Section

Core transformation parameters.

```yaml
transformation:
  inputPath: /path/to/input.json
  outputPath: /path/to/output.json
  batchMode: false
  filePattern: "*.json"
  verbose: false
  parallelProcessing: false
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `inputPath` | String | Required | Path to input QOF JSON file or directory |
| `outputPath` | String | Required | Path to output IMQ JSON file or directory |
| `batchMode` | Boolean | false | Enable batch processing of directories |
| `filePattern` | String | `*.json` | Glob pattern for file selection in batch mode |
| `verbose` | Boolean | false | Enable verbose logging output |
| `parallelProcessing` | Boolean | false | Enable parallel batch processing |

### Performance Section

Resource allocation and tuning parameters.

```yaml
performance:
  maxParallelThreads: 8
  cacheSize: 10000
  batchSize: 100
  maxMemoryUsage: 1073741824
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `maxParallelThreads` | Integer | CPU count | Maximum threads for parallel processing |
| `cacheSize` | Integer | 10000 | Reference mapping cache size |
| `batchSize` | Integer | 100 | Files to process before cache flush |
| `maxMemoryUsage` | Long | JVM max | Maximum memory allowed for transformation |

### Error Handling Section

Error resilience and recovery settings.

```yaml
errorHandling:
  continueOnError: true
  maxRetries: 3
  retryDelayMs: 1000
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `continueOnError` | Boolean | true | Continue batch processing on errors |
| `maxRetries` | Integer | 3 | Number of retry attempts for failures |
| `retryDelayMs` | Long | 1000 | Delay in milliseconds between retries |

### Output Section

Output file handling and formatting.

```yaml
output:
  overwrite: false
  createBackup: true
  encoding: UTF-8
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `overwrite` | Boolean | false | Overwrite existing output files |
| `createBackup` | Boolean | true | Create backup of existing files |
| `encoding` | String | UTF-8 | Output file character encoding |

### Logging Section

Logging configuration.

```yaml
logging:
  level: INFO
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `level` | String | INFO | Log level: TRACE, DEBUG, INFO, WARN, ERROR |

Valid values: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`, `OFF`

### Monitoring Section

Metrics and observability settings.

```yaml
monitoring:
  enableMetrics: false
  metricsOutput: /metrics/output.json
```

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `enableMetrics` | Boolean | false | Enable metrics collection |
| `metricsOutput` | String | None | Path to write metrics output |

## Profiles

Profiles provide pre-configured sets of values for different deployment scenarios.

### Development Profile (`dev`)

Optimized for development and debugging:

```yaml
profile: dev
```

**Settings:**
- Log level: DEBUG
- Metrics enabled: true
- Max parallel threads: 1 (single-threaded)
- Parallel processing: false
- Continue on error: true
- Output overwrite: false

**Use case:** Local development, detailed logging, single-threaded for easier debugging

### Production Profile (`prod`)

Optimized for performance and reliability:

```yaml
profile: prod
```

**Settings:**
- Log level: WARN
- Metrics enabled: false
- Max parallel threads: CPU count
- Parallel processing: true
- Continue on error: true
- Output overwrite: false

**Use case:** Production deployments, maximum performance, minimal logging overhead

### Test Profile (`test`)

Optimized for automated testing:

```yaml
profile: test
```

**Settings:**
- Log level: INFO
- Metrics enabled: false
- Max parallel threads: 1 (single-threaded)
- Parallel processing: false
- Continue on error: false (fail fast)
- Output overwrite: true (clean state)

**Use case:** Automated testing, fast failure on errors, clean output state

## Environment Variables

All configuration options can be overridden using environment variables with the `QOF_` prefix.

### Transformation Environment Variables

```bash
export QOF_INPUT_PATH=/data/input.json
export QOF_OUTPUT_PATH=/data/output.json
export QOF_BATCH_MODE=true
export QOF_FILE_PATTERN="qof-*.json"
export QOF_VERBOSE=true
export QOF_PARALLEL=true
```

### Performance Environment Variables

```bash
export QOF_MAX_THREADS=8
export QOF_CACHE_SIZE=10000
```

### Logging Environment Variables

```bash
export QOF_LOG_LEVEL=DEBUG
```

### Profile Environment Variable

```bash
export QOF_PROFILE=prod
```

All environment variables:

| Variable | Type | Corresponding Option |
|----------|------|----------------------|
| `QOF_INPUT_PATH` | String | `inputPath` |
| `QOF_OUTPUT_PATH` | String | `outputPath` |
| `QOF_BATCH_MODE` | Boolean | `batchMode` |
| `QOF_FILE_PATTERN` | String | `filePattern` |
| `QOF_VERBOSE` | Boolean | `verbose` |
| `QOF_PARALLEL` | Boolean | `parallelProcessing` |
| `QOF_MAX_THREADS` | Integer | `maxParallelThreads` |
| `QOF_CACHE_SIZE` | Integer | `cacheSize` |
| `QOF_LOG_LEVEL` | String | `logLevel` |
| `QOF_PROFILE` | String | `profile` |

## Examples

### Example 1: Basic Single-File Configuration (JSON)

**config.json:**
```json
{
  "transformation": {
    "inputPath": "/data/qof/input.json",
    "outputPath": "/data/imq/output.json"
  },
  "profile": "prod"
}
```

**Usage:**
```bash
java -jar qofimq-cli.jar --config=config.json
```

### Example 2: Batch Processing Configuration (YAML)

**config.yaml:**
```yaml
transformation:
  inputPath: /data/qof-documents
  outputPath: /data/imq-queries
  batch: true
  pattern: "qof-*.json"
  verbose: true

performance:
  maxParallelThreads: 8
  cacheSize: 20000

errorHandling:
  continueOnError: true
  maxRetries: 5

profile: prod
```

**Usage:**
```bash
java -jar qofimq-cli.jar --config=config.yaml
```

### Example 3: Development Configuration with Profiles

**config.yaml:**
```yaml
transformation:
  inputPath: /data/qof
  outputPath: /data/imq
  verbose: false

profile: prod
```

**config-dev.yaml:**
```yaml
transformation:
  verbose: true

logging:
  level: DEBUG

monitoring:
  enableMetrics: true
```

**Usage:**
```bash
# Use development configuration
java -jar qofimq-cli.jar --config=config.yaml --profile=dev

# Or set environment variable
export QOF_PROFILE=dev
java -jar qofimq-cli.jar --config=config.yaml
```

### Example 4: Environment Variable Override

**config.yaml:**
```yaml
transformation:
  inputPath: /default/input.json
  outputPath: /default/output.json

performance:
  maxParallelThreads: 4
```

**Usage with environment variables:**
```bash
export QOF_INPUT_PATH=/override/input.json
export QOF_MAX_THREADS=8

java -jar qofimq-cli.jar --config=config.yaml
# Result:
# - inputPath: /override/input.json (from env)
# - outputPath: /default/output.json (from config)
# - maxParallelThreads: 8 (from env)
```

### Example 5: Command-Line Override

**Usage:**
```bash
java -jar qofimq-cli.jar \
  --config=config.yaml \
  --input=/cli/input.json \
  --output=/cli/output.json \
  --batch \
  --verbose
```

All CLI arguments override configuration file values.

### Example 6: Complete Configuration with All Sections

**config-production.yaml:**
```yaml
# Production transformation configuration
transformation:
  inputPath: /prod/data/qof
  outputPath: /prod/data/imq
  batch: true
  pattern: "*.json"
  verbose: false
  parallel: true

performance:
  maxParallelThreads: 16
  cacheSize: 50000
  batchSize: 500
  maxMemoryUsage: 8589934592  # 8GB

errorHandling:
  continueOnError: true
  maxRetries: 3
  retryDelayMs: 2000

output:
  overwrite: false
  createBackup: true
  encoding: UTF-8

logging:
  level: WARN

monitoring:
  enableMetrics: true
  metricsOutput: /prod/metrics/qofimq-metrics.json

profile: prod
```

## Configuration Validation

Configuration is automatically validated when loaded. Validation errors will prevent execution and warnings will be logged.

### Common Validation Errors

| Error | Solution |
|-------|----------|
| "Input path is required" | Provide `--input` or set `QOF_INPUT_PATH` |
| "Output path is required" | Provide `--output` or set `QOF_OUTPUT_PATH` |
| "Max parallel threads must be >= 1" | Ensure `maxParallelThreads` ≥ 1 |
| "Unknown encoding" | Use valid encoding like `UTF-8`, `ISO-8859-1` |
| "Unknown log level" | Use valid level: TRACE, DEBUG, INFO, WARN, ERROR |

### Validation Warnings

Warnings indicate non-critical configuration issues:

```
WARN: Unknown log level: VERBOSE. Using INFO
WARN: Unknown encoding: LATIN1. Using UTF-8
```

Warnings do not prevent execution but indicate potential configuration issues.

## Best Practices

1. **Use YAML for complex configurations** - More readable than JSON for humans
2. **Create profile-specific files** - Maintain separate dev/prod configurations
3. **Use environment variables for secrets** - Never commit credentials to configuration files
4. **Validate configuration files** - Use `--validate-config` before deploying
5. **Document custom properties** - Add comments explaining custom configuration
6. **Version control configuration** - Track changes to configuration schemas
7. **Test configuration precedence** - Verify override behavior in your environment

## Related Documentation

- [CLI User Guide](CLI_USER_GUIDE.md) - Command-line interface usage
- [Phase 6.2 Implementation](../.zencoder/docs/PHASE_6_2_IMPLEMENTATION.md) - Technical details