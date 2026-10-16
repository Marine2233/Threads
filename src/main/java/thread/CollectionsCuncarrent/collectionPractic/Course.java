package thread.CollectionsCuncarrent.collectionPractic;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Course {
    private final String title;
    private final String code;
    private final int capacity;
}
