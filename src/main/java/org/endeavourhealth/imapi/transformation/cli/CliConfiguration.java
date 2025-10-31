package org.endeavourhealth.imapi.transformation.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Holds CLI configuration state for QOF to IMQ transformation.
 * 
 * Encapsulates all command-line parameters and their validation state.
 */
public class CliConfiguration {

    // Core parameters
    private String inputPath;
    private String outputPath;
    private boolean batchMode = false;
    private String filePattern = "*.json";
    private boolean verbose = false;
    private boolean parallelProcessing = false;

    // Validation state
    private List<String> validationErrors = new ArrayList<>();

    // Getters and setters
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

    public List<String> getValidationErrors() {
        return new ArrayList<>(validationErrors);
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public boolean isValid() {
        return validationErrors.isEmpty();
    }

    @Override
    public String toString() {
        return "CliConfiguration{" +
                "inputPath='" + inputPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", batchMode=" + batchMode +
                ", filePattern='" + filePattern + '\'' +
                ", verbose=" + verbose +
                ", parallelProcessing=" + parallelProcessing +
                ", validationErrors=" + validationErrors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CliConfiguration that = (CliConfiguration) o;
        return batchMode == that.batchMode &&
                verbose == that.verbose &&
                parallelProcessing == that.parallelProcessing &&
                Objects.equals(inputPath, that.inputPath) &&
                Objects.equals(outputPath, that.outputPath) &&
                Objects.equals(filePattern, that.filePattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputPath, outputPath, batchMode, filePattern, verbose, parallelProcessing);
    }
}