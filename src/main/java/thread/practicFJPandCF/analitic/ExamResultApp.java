package thread.practicFJPandCF.analitic;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExamResultApp {
    public static void main(String[] args) {
        List<ExamResult> testData = generateTestData(50_000);

        for (int i = 0; i < 3; i++) {
            long startSeq = System.currentTimeMillis();
            Statistic seqStats = sequentialStatistic(testData, false);
            long endSeq = System.currentTimeMillis();
            long seqTime = endSeq - startSeq;


            long startParallel = System.currentTimeMillis();
            Statistic parallel = parallel(testData, true);
            long endParallel = System.currentTimeMillis();
            long parallelTime = endParallel - startParallel;

            System.out.printf("Линейный анализ: %s мс\n", seqTime);
            System.out.printf("Параллельный анализ:     %s мс\n", parallelTime);
        }


    }

    private static List<ExamResult> generateTestData(int count) {
        List<ExamResult> list = new ArrayList<>();
        String[] studentNames = {"Иван", "Анна", "Петр", "Мария", "Олег", "Елена", "Дмитрий"};
        Subject[] subjects = Subject.values();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            long id = random.nextInt(1000);
            Student student = new Student(id, studentNames[random.nextInt(studentNames.length)]);

            Subject subject = subjects[random.nextInt(subjects.length)];

            int score = 50 + random.nextInt(51);

            list.add(new ExamResult(score, student, subject));
        }
        return list;
    }

    public static Statistic sequentialStatistic(List<ExamResult> results, boolean is) {
        CopyOnWriteArrayList<ExamResult> copy = new CopyOnWriteArrayList<>(results);
        Statistic statistic = new Statistic(copy, is);
        Map<Subject, Double> avg = new HashMap<>();
        Map<Subject, Long> maxBall = new HashMap<>();
        Map<Student, Map<Subject, Double>> topStudent = new HashMap<>();


            avg = statistic.avgBallAllSubject(is);
            System.out.println("Avg: " + avg);

            maxBall = statistic.maxBall(is);
            System.out.println("Max ball: " + maxBall);

            topStudent = statistic.topStudent(is);
            System.out.println("Top studs: " + topStudent);


        return new Statistic(avg, maxBall, topStudent);

    }

    public static Statistic parallel(List<ExamResult> results, boolean is) {
        CopyOnWriteArrayList<ExamResult> copy = new CopyOnWriteArrayList<>(results);
        Statistic statistic = new Statistic(copy, is);

        Map<Subject, Double> avg = new HashMap<>();
        Map<Subject, Long> maxBall = new HashMap<>();
        Map<Student, Map<Subject, Double>> topStudent = new HashMap<>();

            avg = statistic.avgBallAllSubject(is);
            System.out.println("Avg: " + avg);

            maxBall = statistic.maxBall(is);
            System.out.println("Max ball: " + maxBall);

            topStudent = statistic.topStudent(is);
            System.out.println("Top studs: " + topStudent);

        return new Statistic(avg, maxBall, topStudent);
    }
}
