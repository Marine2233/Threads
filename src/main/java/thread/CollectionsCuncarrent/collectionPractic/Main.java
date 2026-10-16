package thread.CollectionsCuncarrent.collectionPractic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        List<Student> students = students(15);
        List<Course> courses = generateCourses();

        CourseRegistry registry = new CourseRegistry();

        students.forEach(registry::addStudent);
        courses.forEach(registry::addCourse);


        //       boolean firstEnroll = registry.enroll(students.get(0).getId(), "JAVA");
//        boolean secondEnroll = registry.enroll(students.get(0).getId(), "JAVA");
//        System.out.printf("Запись студента 0 на Java (попытка 1): %b\n", firstEnroll);
//        System.out.printf("Запись студента 0 на Java (попытка 2): %b\n", secondEnroll);

        for (int i = 1; i <= 5; i++) {
            boolean enrolled = registry.enroll(students.get(i).getId(), "GO");
            System.out.printf("Запись студента %d на курс GO: %b\n", i, enrolled);
        }


        long studentToCancel = students.get(1).getId();
        boolean canceled = registry.cancelEnroll(studentToCancel, "GO");
        System.out.printf("\nОтмена записи студента %d с курса GO: %b\n", studentToCancel, canceled);
        System.out.printf("Свободных мест на GO теперь: %d\n", registry.freePlaces("GO"));

        boolean newEnroll = registry.enroll(students.get(10).getId(), "GO");
        System.out.printf("Запись нового студента 10 на освободившееся место курса GO: %b\n", newEnroll);

        for (int i = 2; i < students.size(); i++) {
            registry.enroll(students.get(i).getId(), "JAVA");
            if (i % 2 == 0) {
                registry.enroll(students.get(i).getId(), "PYTHON");
            }
            if (i % 3 == 0) {
                registry.enroll(students.get(i).getId(), "JS");
            }
        }

        for (Course c : courses) {
            int free = registry.freePlaces(c.getCode());
            System.out.printf(" - Курс: %-20s | Код: %-7s | Свободно мест: %d/%d\n",
                    c.getTitle(), c.getCode(), free, c.getCapacity());
        }

        for (Course c : courses) {
            List<Student> enrolledStudents = registry.students(c.getCode());
            System.out.printf(" * %s (%s): ", c.getTitle(), c.getCode());
            if (enrolledStudents.isEmpty()) {
                System.out.println("На этот курс еще никто не записан.");
            } else {
                System.out.println(enrolledStudents);
            }
        }

        Map<String, Long> rating = registry.findCountStudentsFromCourse();
            rating.forEach((code, count) ->
                    System.out.printf("Код курса: %-7s — Всего студентов: %d\n", code, count)
            );
    }

        public static List<Course> generateCourses() {
        List<Course> list = new ArrayList<>();
        list.add(new Course("Java Programming", "JAVA", 15));
        list.add(new Course("C# Net Core", "CSHARP", 10));
        list.add(new Course("Python Data Science", "PYTHON", 20));
        list.add(new Course("JavaScript Frontend", "JS", 12));
        list.add(new Course("Go Microservices", "GO", 3)); // Маленькая вместимость для теста заполнения
        return list;
    }

    public static List<Student>students(int count){
        List<Student>students = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int age = random.nextInt(18,30);
            students.add(new Student(i,"stud_"+i,age));
        }
    return students;
    }
}
