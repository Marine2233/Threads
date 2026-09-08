package thread.practicWorkByBaseTopicsThread.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TrainType {
        PASSENGER("Пассажирский"),
        CARGO("Грузовой"),
        EXPRESS("Експресс"),
        EMERGENCY("Пожарный");
        private String value;

        TrainType(String value){
            this.value = value;
        }
        @JsonCreator
        public static TrainType create(String value){
            if (value == null){
                return null;
            }

            for (TrainType type: values()){
                if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)){
                    return type;
                }
            }throw new  RuntimeException("Несуществующий тип.");
        }
        @JsonValue
        public String getValue() {
        return value;
    }
}
