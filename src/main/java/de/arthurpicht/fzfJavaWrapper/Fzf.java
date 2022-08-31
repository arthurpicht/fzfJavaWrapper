package de.arthurpicht.fzfJavaWrapper;

import de.arthurpicht.processExecutor.ProcessExecution;
import de.arthurpicht.processExecutor.ProcessExecutionException;
import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.utils.io.nio2.FileUtils;
import de.arthurpicht.utils.io.tempDir.TempDir;
import de.arthurpicht.utils.io.tempDir.TempDirs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Fzf {

    public String execute(List<String> inputElements) throws IOException {

        TempDir tempDir = TempDirs.createUniqueTempDirAutoRemove(FileUtils.getWorkingDir());
        Path resultFile = tempDir.asPath().resolve("fzf_result");

        try {
//            ProcessExecution.executeInteractive("/bin/bash", "-c", "echo -e 'element1\\nelement2\\nelement3' | fzf | cat >result.txt");
            ProcessExecution.executeInteractive(
                    "/bin/bash", "-c",
                    getEchoElementsCommand(inputElements) +
                    " | fzf | " +
                    getWriteToTempResultFileCommand(resultFile));
        } catch (ProcessExecutionException e) {
            throw new RuntimeException(e);
        }

        return "dummy";
    }

    private String getEchoElementsCommand(List<String> inputElements) {
        String elements = Strings.listing(inputElements, "\n");
        return "echo -e '" + elements + "'";
    }

    private String getWriteToTempResultFileCommand(Path tempResultFile) {
        return "cat >'" + tempResultFile.toAbsolutePath() + "'";
    }

}
