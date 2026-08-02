package thread.HWThread.processDownload;

public class AppDownload {
    public static void main(String[] args) {
            String[] files = {
                    "photo.jpg",
                    "movie.mp4",
                    "music.mp3"
            };

            System.out.println("МЕНЕДЖЕР ЗАГРУЗОК:\n");
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i];

                FileDownload task = new FileDownload(fileName);
                new Thread(task, "Downloader-" + (i + 1)).start();

            }
        }
    }

