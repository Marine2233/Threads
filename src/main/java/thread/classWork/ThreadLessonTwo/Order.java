package thread.classWork.ThreadLessonTwo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Order {
    private long id;
    private String customer;
    @Setter
    private volatile OrderStatus status;

    public Order(String customer, long id) {
        this.customer = customer;
        this.id = id;
        status = OrderStatus.CREATED;
    }

    public synchronized boolean startIssuing(){
        if (status == OrderStatus.READY){
            status = OrderStatus.ISSUING;
            return true;
        }
        return false;
    }

    public synchronized void completeIssued(){
        if (status ==OrderStatus.ISSUING){
            status = OrderStatus.ISSUED;
        }
    }

}
