package thread.classWork.aircraft;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Aircraft {
    private final long id;
    private final String flightNumber;
    private final AircraftType type;

    public Aircraft(long id, String flightNumber, AircraftType type) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.type = type;
    }


}
