package thread.javaBaseThread;

import lombok.Getter;
import lombok.ToString;

import java.util.Random;
import java.util.concurrent.Callable;

@Getter
@ToString
public class FileTask  {
    private final long id;
    private final String fileName;
    private final long fileSize;
    private static int count = 0;

    public FileTask(String fileName) {
        Random random = new Random();
        this.fileName = fileName;
        this.id = ++count;
        this.fileSize = random.nextInt(1,75);
    }
}
