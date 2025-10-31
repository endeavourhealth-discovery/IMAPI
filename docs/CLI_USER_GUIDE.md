# QOF to IMQ Transformation CLI User Guide

## Overview

The QOF to IMQ CLI is a command-line tool for transforming Quality and Outcomes Framework (QOF) documents into Information Model Query (IMQ) format. It supports both single-file and batch directory transformations with configurable output paths, verbose logging, and error resilience.

## Installation

### Prerequisites
- Java 21 or later
- 2GB minimum RAM for batch processing
- Network access (if using remote data sources)

### Setup

1. Download the executable Fat JAR: `qofimq-cli.jar`
2. Place it in your preferred location
3. Ensure Java is in your system PATH or set `JAVA_HOME` environment variable

## Quick Start

### Single File Transformation

Transform a single QOF JSON document:

```bash
java -jar qofimq-cli.jar \
  --input=/path/to/qof-document.json \
  --output=/path/to/imq-query.json
```

### Batch Directory Processing

Transform all JSON files in a directory:

```bash
java -jar qofimq-cli.jar \
  --batch \
  --input=/path/to/qof-documents \
  --output=/path/to/imq-queries
```

### With Verbose Logging

Enable detailed logging output:

```bash
java -jar qofimq-cli.jar \
  --input=/path/to/qof-document.json \
  --output=/path/to/imq-query.json \
  --verbose
```

## Command-Line Options

### Required Options

#### `--input=<path>`
- **Description:** Path to input file or directory
- **Single-file mode:** Must be a valid QOF JSON file
- **Batch mode:** Must be a directory
- **Example:** `--input=/data/qof-documents/sample.json`

#### `--output=<path>`
- **Description:** Path to output file or directory
- **Single-file mode:** Output JSON file path (parent directory must exist)
- **Batch mode:** Output directory (will be created if it doesn't exist)
- **Example:** `--output=/data/imq-queries`

### Optional Options

#### `--batch`
- **Description:** Enable batch mode for processing multiple files
- **Default:** Disabled (single-file mode)
- **Example:** `--batch`

#### `--pattern=<pattern>`
- **Description:** Glob pattern for selecting files in batch mode
- **Default:** `*.json`
- **Format:** Standard glob patterns (e.g., `qof-*.json`, `**/*.json`)
- **Example:** `--pattern="qof-*.json"`
- **Note:** Only used when `--batch` is specified

#### `--verbose`
- **Description:** Enable verbose logging output
- **Default:** Disabled
- **Example:** `--verbose`

#### `--config=<path>`
- **Description:** Path to JSON configuration file
- **Format:** JSON with keys: `input`, `output`, `batch`, `pattern`, `verbose`, `parallelProcessing`
- **Precedence:** Command-line options override config file values
- **Example:** `--config=/etc/qofimq-config.json`

#### `--help` or `-h`
- **Description:** Display help message with usage examples
- **Example:** `java -jar qofimq-cli.jar --help`

#### `--parallel` (Batch mode only)
- **Description:** Enable parallel processing for faster batch transformations
- **Default:** Disabled (sequential processing)
- **Note:** Use with caution on systems with limited resources
- **Example:** `--batch --parallel`

## Configuration Files

### JSON Configuration Format

Create a configuration file `qofimq-config.json`:

```json
{
  "input": "/path/to/input",
  "output": "/path/to/output",
  "batch": false,
  "pattern": "*.json",
  "verbose": false,
  "parallelProcessing": false
}
```

### Configuration Precedence

1. Command-line arguments (highest priority)
2. Configuration file values
3. Default values (lowest priority)

### Example Configuration File

```json
{
  "input": "/data/qof-documents",
  "output": "/data/imq-queries",
  "batch": true,
  "pattern": "qof-*.json",
  "verbose": true,
  "parallelProcessing": true
}
```

## Usage Examples

### Example 1: Simple Single File Transformation

Transform a single QOF document:

```bash
java -jar qofimq-cli.jar \
  --input=/home/user/qof-sample.json \
  --output=/home/user/imq-output.json
```

### Example 2: Batch Processing with Pattern

Transform all files matching a pattern:

```bash
java -jar qofimq-cli.jar \
  --batch \
  --input=/data/qof-documents \
  --output=/data/imq-queries \
  --pattern="qof-*.json" \
  --verbose
```

### Example 3: Parallel Batch Processing

Process multiple files in parallel:

```bash
java -jar qofimq-cli.jar \
  --batch \
  --input=/data/large-document-set \
  --output=/data/results \
  --parallel \
  --verbose
```

### Example 4: Configuration File Usage

Use a configuration file:

```bash
java -jar qofimq-cli.jar --config=/etc/qofimq.json
```

### Example 5: Override Configuration File

Use configuration file but override specific values:

```bash
java -jar qofimq-cli.jar \
  --config=/etc/qofimq.json \
  --input=/alternative/input/path
```

## Output

### Single File Mode

- **Success:** IMQ Query JSON written to specified output file
- **Failure:** Error message printed to stderr, exit code 1

### Batch Mode

- **Progress:** Real-time progress bar showing transformation status
- **Report:** Summary statistics after completion
  - Total files processed
  - Successful transformations
  - Failed transformations
  - Average processing time
  - Success rate percentage

## Exit Codes

| Code | Meaning |
|------|---------|
| 0 | Transformation completed successfully |
| 1 | Transformation failed (see error messages) |

## Error Handling

### Common Errors

#### Input file not found
```
Error: Invalid configuration
Validation errors:
  - Input path does not exist: /path/to/file.json
Use --help for usage information
```

**Solution:** Verify the input file path and ensure the file exists

#### Output directory cannot be created
```
Error: Invalid configuration
Validation errors:
  - Cannot create output directory: Permission denied
Use --help for usage information
```

**Solution:** Ensure you have write permissions to the parent directory

#### Transformation failed
```
[ERROR] Single file transformation failed: Invalid QOF document structure
```

**Solution:** 
- Check the QOF document is valid JSON
- Verify it contains required fields (name, selections, registers)
- Use `--verbose` for detailed error information

### Batch Processing Failures

If some files fail during batch processing:
- The CLI continues processing remaining files
- Failed files are listed in the final report
- Exit code is 1 (indicates partial or complete failure)

### Verbose Error Output

For detailed error information, use the `--verbose` flag:

```bash
java -jar qofimq-cli.jar \
  --input=/path/to/qof.json \
  --output=/path/to/imq.json \
  --verbose
```

## Performance Considerations

### Single File Processing
- Typical processing time: 100-500ms per document
- Memory usage: ~100-300MB depending on document size

### Batch Processing
- Sequential: ~1000 documents in 30-60 seconds
- Parallel: ~1000 documents in 10-20 seconds (with multi-core CPU)

### Memory Usage
- Minimum: 512MB (tight)
- Recommended: 2GB for batch processing
- Large batches (10K+ files): 4GB+ recommended

### Optimization Tips
1. Use `--parallel` for large batches with multi-core systems
2. Process in smaller batches if memory is limited
3. Use specific patterns (`--pattern="qof-*.json"`) to reduce I/O overhead
4. Disable `--verbose` in production for better performance

## Troubleshooting

### CLI won't start

**Issue:** `ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH`

**Solution:** Set JAVA_HOME environment variable:
```bash
# Linux/Mac
export JAVA_HOME=/path/to/java21

# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-21
```

### Out of Memory Error

**Issue:** `java.lang.OutOfMemoryError: Java heap space`

**Solution:** Increase heap size:
```bash
java -Xmx4g -jar qofimq-cli.jar --input=... --output=...
```

### Slow Processing

**Issue:** Batch processing is taking too long

**Solution:**
1. Enable parallel processing: `--parallel`
2. Increase heap size for better GC performance
3. Check system load with `top` or Task Manager
4. Process in smaller batches

### File Permission Issues

**Issue:** `Permission denied` errors

**Solution:**
- Ensure read permissions for input directory
- Ensure write permissions for output directory
- Run with appropriate user privileges

## Advanced Usage

### Scripting

Use the CLI in shell scripts:

```bash
#!/bin/bash

INPUT_DIR="/data/qof-documents"
OUTPUT_DIR="/data/imq-queries"

java -jar qofimq-cli.jar \
  --batch \
  --input="$INPUT_DIR" \
  --output="$OUTPUT_DIR" \
  --verbose

if [ $? -eq 0 ]; then
  echo "Transformation successful"
else
  echo "Transformation failed"
  exit 1
fi
```

### Docker Usage

Run the CLI in a Docker container:

```dockerfile
FROM openjdk:21-slim

COPY qofimq-cli.jar /app/
WORKDIR /app

ENTRYPOINT ["java", "-jar", "qofimq-cli.jar"]
```

Usage:
```bash
docker build -t qofimq-cli .
docker run -v /local/data:/data qofimq-cli \
  --input=/data/qof-documents \
  --output=/data/imq-queries
```

## Support and Documentation

- **GitHub:** https://github.com/endeavourhealth-discovery/IMAPI
- **Issues:** https://github.com/endeavourhealth-discovery/IMAPI/issues
- **Documentation:** See `/docs` directory in the repository

## Version Information

Get version information:
```bash
java -jar qofimq-cli.jar --help
```

The help message includes version and build information in the JAR manifest.

## License

See LICENSE file in the repository for license information.