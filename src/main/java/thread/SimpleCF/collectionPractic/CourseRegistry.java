package thread.SimpleCF.collectionPractic;

import java.util.*;
import java.util.stream.Collectors;

public class CourseRegistry {

    private final Map<Long,Student> studentsMap = new HashMap<>();
    private final Map<String,Course> coursesMap = new TreeMap<>();
    private  final Map<String, Set<Long>> enrollElements = new HashMap<>();

    /**
     * @return - карту(LHM ) где ключ - это курс, а значение - кол-во студентов
     * */
    public Map<String,Long> findCountStudentsFromCourse(){
        return enrollElements.entrySet().stream().
               collect(Collectors.toMap(
                       Map.Entry::getKey,
                       entry-> (long) entry.getValue().size(),
                       Long::sum,
                       LinkedHashMap::new
               ));
    }

    /**
     * @return - список топовых курсов
     * */
    public List<Course> topCourse(){
        return   enrollElements.keySet().stream().
                map(coursesMap::get).
                filter(Objects::nonNull).
                sorted(Comparator.comparingInt((Course c)-> enrollElements.getOrDefault(c.getCode(),Set.of()).size()).reversed()).
                toList();
    }

    /**
     * @param courseCode - название курса
     * @return  - кол-во свободных мест на курсе
     * @exception IllegalArgumentException - если аргумент = null
     * @exception RuntimeException - если курс не существует
     *
     * */
    public int freePlaces(String courseCode){

        if (courseCode == null){
            throw new IllegalArgumentException("Пустой элемент.");
        }

        if(!coursesMap.containsKey(courseCode)){
            throw new RuntimeException("Курс не существует.");
        }

        Course course = coursesMap.get(courseCode);

        if (course == null){
            throw new RuntimeException("Данные о курсе повреждены.");
        }

        int enrolledStuds = enrollElements.getOrDefault(courseCode,Set.of()).size();

        return course.getCapacity() - enrolledStuds;

    }

    /**
     * @param studId - id студента
     * @return - список курсов для конкретного студента
     * */
    public List<Course> courses(long studId){
        return enrollElements.entrySet().stream().
                filter(entry-> entry.getValue().contains(studId)).
                map(Map.Entry::getKey).
                map(coursesMap::get).
                filter(Objects::nonNull).
                toList();
    }

    /**
     * @param courseCode- название курса
     * @return - список студентов курса
     * */
    public List<Student> students(String courseCode){
        Set<Long> studs = enrollElements.getOrDefault(courseCode,Set.of());

        return studs.stream().
                filter(Objects::nonNull).
                map(studentsMap::get).
                filter(Objects::nonNull).
                toList();
    }

    /**
     * @param studId- id студента
     * @param courseCode- название курса
     * @return - истина, если отписка успешна
     * */
    public boolean cancelEnroll(long studId, String courseCode){
        Set<Long>studIds = enrollElements.get(courseCode);
        return studIds.remove(studId);

    }

    /**
     * @param studId- id студента
     * @param courseCode- название курса
     * @return - истина, если запись успешна
     * */
    public boolean enroll(long studId, String courseCode){
        Student student = studentsMap.get(studId);
        Course course = coursesMap.get(courseCode);

        if (student == null || course == null){
            return  false;
        }

        Set<Long>studs = enrollElements.computeIfAbsent(courseCode,k-> new HashSet<>());
        if (studs.size() >= course.getCapacity() && !studs.contains(studId)){
            return false;
        }

        return studs.add(studId);
    }

    /**
     * @param course- объект курса
     * @return - истина, если курса в списке не было - добавит.
     * */
    public boolean addCourse(Course course){
        Course course1 = coursesMap.putIfAbsent(course.getCode(), course);
        return course1 == null;
    }

    /**
     * @param student - объект студента
     * @return - истина, если в списке не было - добавит.
     * */
    public boolean addStudent(Student student){
        Student student1 = studentsMap.putIfAbsent(student.getId(), student);
        return student1 == null;
    }
}
