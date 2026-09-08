package thread.practicWorkByBaseTopicsThread.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@ToString
public class Trac {
    private final int id;
    private final String name;
    private final String platform;
    private final Lock lock = new ReentrantLock(true);

    public Trac(int id){
        Random random = new Random();
        this.id = id;
        name = "Trac: " + id;
        int plat = random.nextInt(1,3);
        platform = "Platform: " + plat;

    }

    public int getQueueLength(){
        return ((ReentrantLock)lock).getQueueLength();
    }

    public boolean isOverloaded(){
        return getQueueLength() > 3;
    }

}
