package thread.javaBaseThread;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.Callable;
@Getter
@ToString
public class FileProcessor implements Callable<FileResult> {
    private FileTask task;

    public FileProcessor(FileTask task) {
        this.task = task;

    }

    @Override
    public FileResult call()  {
        System.out.printf("\nWorker: %s started.\nFile- %s; size- %s" ,
                Thread.currentThread().getName(),task.getFileName(),task.getFileSize());
            try {
                Thread.sleep(1000);
                FileResult fileResult = new FileResult(task.getFileName(), task.getId(), task.getFileSize(), Thread.currentThread().getName());
                ProcessingStatistic.incProcessCompleted();
                return fileResult;
            } catch (InterruptedException e) {
                ProcessingStatistic.incProcessInterrupted();
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupt.");
                return null;
            }
    }
}
