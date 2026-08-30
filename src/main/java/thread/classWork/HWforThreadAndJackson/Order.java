package thread.classWork.HWforThreadAndJackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private long id;
    @JsonAlias({"customer_name","customer","name"})
    @JsonSetter(nulls = Nulls.FAIL)
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
