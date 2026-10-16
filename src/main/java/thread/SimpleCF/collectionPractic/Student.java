package thread.SimpleCF.collectionPractic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Student {
    private final long id;
    private final String name;
    private final int age;

}
