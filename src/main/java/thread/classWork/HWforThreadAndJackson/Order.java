package thread.classWork.HWforThreadAndJackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
    private long id;
    private String customer;
    private long price;
    private OrderStatus status;

    public synchronized boolean startProcessing(){
        if (status.getValueNane().equalsIgnoreCase(OrderStatus.NEW.getValueNane()) || status.name().equalsIgnoreCase(OrderStatus.NEW.name())){
            status = OrderStatus.PROCESSING;
            return true;
        }
        return false;
    }

    public synchronized void complete(){
        if (status.getValueNane().equalsIgnoreCase(OrderStatus.PROCESSING.getValueNane()) ||
        status.name().equalsIgnoreCase(OrderStatus.PROCESSING.name())){
            status = OrderStatus.DONE;
        }
    }

    public synchronized void fail(){
       status = OrderStatus.FAILED;
    }

}
