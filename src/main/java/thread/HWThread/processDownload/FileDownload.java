package thread.HWThread.processDownload;

public class FileDownload implements Runnable {
    private final String fileName;

    public FileDownload(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        for (int progress = 0; progress <= 100; progress += 10) {
            System.out.printf("[%s] Загрузка %s: %d%%\n", threadName, fileName, progress);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.printf("[%s] Загрузка файла %s была прервана!\n", threadName, fileName);
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.printf("Файл %s успешно загружен.\n", fileName);
    }
}
