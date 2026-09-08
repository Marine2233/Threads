package thread.practicWorkByBaseTopicsThread.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TrainStatus {
        CREATED("Сформирован"),
        WAITING("Ожидает"),
        ON_TRAC("Прибыл"),
        SERVICING("Обслуживается"),
        READY_TO_DEPART("Готов к отправлению"),
        DEPARTED("Отправился"),
        FAILED("Убыл");
        private String value;

        TrainStatus(String value){
            this.value = value;
        }

        @JsonCreator
        public static TrainStatus createStatus(String value){
            if (value == null){
                return null;
            }

            for (TrainStatus status: values()){
                if (status.value.equalsIgnoreCase(value) || value.equalsIgnoreCase(status.name())){
                    return status;
                }
            }throw new RuntimeException("Несуществующий статус.");
        }
    @JsonValue
    public String getValue() {
        return value;
    }
}
