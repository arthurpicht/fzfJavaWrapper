package de.arthurpicht.fzfJavaWrapper;

import de.arthurpicht.fzfJavaWrapper.exception.FzfRuntimeException;
import de.arthurpicht.fzfJavaWrapper.exception.InvalidSelectionException;
import de.arthurpicht.processExecutor.ProcessExecution;
import de.arthurpicht.processExecutor.ProcessExecutionException;
import de.arthurpicht.processExecutor.ProcessExecutor;
import de.arthurpicht.processExecutor.ProcessExecutorBuilder;
import de.arthurpicht.processExecutor.outputHandler.StandardErrorCollectionHandler;
import de.arthurpicht.processExecutor.outputHandler.StandardOutCollectionHandler;
import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.utils.io.file.SingleValueFile;
import de.arthurpicht.utils.io.nio2.FileUtils;
import de.arthurpicht.utils.io.tempDir.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Fzf {

    private final boolean noSort;
    private final boolean tac;
    private final Integer heightLines;
    private final Integer heightPercent;
    private final boolean layoutReverse;
    private final boolean layoutReverseList;
    private final String prompt;
    private final Path tempParentDir;
    private final Path fzfBinary;

    public static FzfBuilder builder() {
        return new FzfBuilder();
    }

    /**
     * Use {@link FzfBuilder} to create instance.
     *
     * @param noSort --no-sort flag
     * @param tac --tag flag
     * @param heightLines --height flag for line numbers
     * @param heightPercent --height flag for percentage
     * @param layoutReverse --layout=reverse parameter
     * @param layoutReverseList --layout=reverse-list parameter
     * @param prompt --prompt=STRING parameter
     * @param tempParentDir parent directory for temporary files
     * @param fzfBinary path to fzf binary
     */
    public Fzf(
            boolean noSort,
            boolean tac,
            Integer heightLines,
            Integer heightPercent,
            boolean layoutReverse,
            boolean layoutReverseList,
            String prompt,
            Path tempParentDir,
            Path fzfBinary) {

        this.noSort = noSort;
        this.tac = tac;
        this.heightLines = heightLines;
        this.heightPercent = heightPercent;
        this.layoutReverse = layoutReverse;
        this.layoutReverseList = layoutReverseList;
        this.prompt = prompt;
        this.tempParentDir = tempParentDir;
        this.fzfBinary = fzfBinary;

        if (this.tempParentDir != null) {
            if (!FileUtils.isExistingDirectory(this.tempParentDir))
                throw new FzfRuntimeException("Specified parent directory for temporary files is not existing: "
                        + this.tempParentDir.toAbsolutePath());
            if (!Files.isWritable(tempParentDir))
                throw new FzfRuntimeException("No write permissions for specified parent for temporary files: "
                        + this.tempParentDir.toAbsolutePath());
        }

        if (this.fzfBinary != null) {
            if (!FileUtils.isExistingRegularFile(this.fzfBinary))
                throw new FzfRuntimeException("Specified fzf binary is not existing.");
        }

        try {
            // This should be used instead. Not working as ProcessExecution imports Logger which is actually not needed.
//            ProcessResultCollection result = ProcessExecution.execute("command", "-v", "fzf");

            StandardOutCollectionHandler stdOutHandler = new StandardOutCollectionHandler();
            StandardErrorCollectionHandler stdErrorHandler = new StandardErrorCollectionHandler();
            ProcessExecutor processExecutor = new ProcessExecutorBuilder()
                    .withCommands("which", "fzf")
                    .withStandardOutHandler(stdOutHandler)
                    .withStandardErrorHandler(stdErrorHandler)
                    .build();
            processExecutor.execute();
            if (processExecutor.getExitCode() != 0) throw new FzfRuntimeException("Command fzf not found on path.");
        } catch (ProcessExecutionException e) {
            throw new FzfRuntimeException(e.getMessage(), e);
        }

    }

    /**
     * Executes fzf for specified input elements. Returns chosen element or null if no choice was made.
     *
     * @param inputElements input elements to chose from
     * @return chosen element, null is no choice was made
     */
    public String execute(List<String> inputElements) throws InvalidSelectionException {

        TempDir tempDir = createTempDir();
        Path resultFile = tempDir.asPath().resolve("out");
        Path inputFile = tempDir.asPath().resolve("in");

        try {
            prepareInputFile(inputElements, inputFile);

            ProcessExecution.executeInteractive(
                    "/bin/bash", "-c",
                    getCatInputFileCommand(inputFile) +
                    " | " + getFzfCommand() + " | " +
                    getWriteToTempResultFileCommand(resultFile));

            return obtainResult(resultFile);

        } catch (ProcessExecutionException | IOException e) {
            throw new FzfRuntimeException(e.getMessage(), e);
        } finally {
            tempDir.remove();
        }
    }

    private TempDir createTempDir() {
        TempDir.Creator tempDirCreator = new TempDir.Creator();
        if (this.tempParentDir != null)
            tempDirCreator.withParentDir(this.tempParentDir);
        return tempDirCreator.create();
    }

    private void prepareInputFile(List<String> inputElements, Path inputFile) throws IOException {
        String elements = Strings.listing(inputElements, "\n");
        Files.writeString(inputFile, elements);
    }

    private String getCatInputFileCommand(Path inputFile) {
        return "cat '" + inputFile.toAbsolutePath() + "'";
    }

    private String getFzfCommand() {

        String command = "";

        if (this.fzfBinary != null) {
            command += fzfBinary.toAbsolutePath();
        } else {
            command += "fzf";
        }

        if (this.noSort) {
            command += " --no-sort";
        }
        if (this.tac) {
            command += " --tac";
        }
        if (this.heightLines != null) {
            command += " --height=" + this.heightLines;
        }
        if (this.heightPercent != null) {
            command += " --height=" + this.heightPercent + "%";
        }
        if (this.layoutReverse) {
            command += " --layout=reverse";
        }
        if (this.layoutReverseList) {
            command += " --layout=reverse-list";
        }
        if (this.prompt != null) {
            command += " --prompt='" + this.prompt + "'";
        }
        return command;
    }

    private String getWriteToTempResultFileCommand(Path tempResultFile) {
        return "cat >'" + tempResultFile.toAbsolutePath() + "'";
    }

    private String obtainResult(Path tempResultFile) throws InvalidSelectionException {
        if (FileUtils.isExistingRegularFile(tempResultFile)) {
            SingleValueFile singleValueFile = new SingleValueFile(tempResultFile);
            try {
                String value = singleValueFile.read();
                if (Strings.isUnspecified(value)) {
                    throw new InvalidSelectionException();
                } else {
                    return value;
                }
            } catch (IOException e) {
                throw new FzfRuntimeException("Temp file could not be read. " + e.getMessage(), e);
            }
        }
        throw new FzfRuntimeException("Temp file not found.");
    }

}
