package thread.ThreadPool;

import java.util.concurrent.locks.ReentrantLock;

public class MeetingRoom {

    private final int id;
    private final String name;
    private final ReentrantLock lock;

    public MeetingRoom(int id, String name,boolean fair) {
        this.id = id;
        this.name = name;
        this.lock = new ReentrantLock(fair);
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ReentrantLock getLock(){
        return lock;
    }


}
