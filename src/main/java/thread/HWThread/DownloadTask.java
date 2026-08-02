package thread.HWThread;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloadTask implements Runnable {
    private String fieldsName;
    private int parts;

    @Override
    public void run() {
        System.out.println("Загрузка файла-> " +fieldsName);
        for (int i = 0; i <= parts;i++){
            System.out.printf("Часть %s из %s %n",i,parts);
        }
        System.out.println("Загрузка завершена.");

    }
}
