package thread.practic;

import lombok.Getter;

import java.util.Random;

@Getter
public class Document {
    private volatile DocumentStatus status;
    private long id;
    private String name;

    public Document(String name) {
        Random random = new Random();
        id = random.nextInt(100,999999);
        this.name = name;
        this.status = DocumentStatus.CREATED;
    }

    public void saved(){
        try {
            Thread.sleep(1000);
            if (status == DocumentStatus.CONVERTED) {
                status = DocumentStatus.SAVED;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void converted(){
        try {
            Thread.sleep(2000);
            if (status == DocumentStatus.VALIDATED) {
                status = DocumentStatus.CONVERTED;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public boolean isValid(Document document){
        if (document.getId()%8 == 0 && document.status == DocumentStatus.LOADED){
            document.status = DocumentStatus.VALIDATED;
            return true;
        }
        document.status = DocumentStatus.FAILED;
        return false;
    }

    public void loaded(){
        try {
            Thread.sleep(1000);
            if (status == DocumentStatus.CREATED) {
                status = DocumentStatus.LOADED;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


}
