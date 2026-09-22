package thread.javaExecutorService;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FileResult{
    private final long taskId;
    private final String fileName;
    private final long processedBytes;
    private final String workerName;

    public FileResult(String fileName, long taskId, long processedBytes, String workerName) {
        this.fileName = fileName;
        this.taskId = taskId;
        this.processedBytes = processedBytes;
        this.workerName = workerName;
    }
}
