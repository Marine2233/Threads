package thread.classWork.HWforThreadAndJackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    NEW("Новый заказ"),
    PROCESSING("В сборке"),
    DONE("Выполнен"),
    FAILED("Отменен");
    private String valueNane;

    OrderStatus(String valueNane){
        this.valueNane = valueNane;
    }

    @JsonCreator
    public static OrderStatus createValue(String value){
        if (value == null){
            return  null;
        }

        for (OrderStatus status:values()){
            if (value.equalsIgnoreCase(status.valueNane) || status.name().equalsIgnoreCase(value))
                return status;
        }throw new RuntimeException("Статус не существует.");
    }

    @JsonValue
    public String getValueNane(){
        return valueNane;
    }
}
