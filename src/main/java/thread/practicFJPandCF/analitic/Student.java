package thread.practicFJPandCF.analitic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Student {
    private final long id;
    private final String name;

    public Student(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
