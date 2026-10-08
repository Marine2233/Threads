package thread.practicFJPandCF.analitic;

import lombok.Getter;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Getter
public class Statistic {
    private CopyOnWriteArrayList<ExamResult> results;
    private boolean isParallel;
    private Map<Subject, Double> avgBallAllSubject;
    private Map<Subject, Long> maxBall;
    private Map<Student, Map<Subject, Double>> topStudent;
    public Statistic(Map<Subject, Double> avgBallAllSubject, Map<Subject, Long> maxBall, Map<Student, Map<Subject, Double>> topStudent){
        this.avgBallAllSubject = avgBallAllSubject;
        this.maxBall = maxBall;
        this.topStudent = topStudent;

    }

    public Statistic(CopyOnWriteArrayList<ExamResult> results,boolean isParallel) {
        this.results = results;
        this.isParallel = isParallel;
    }

    private static void heavyCpuLoad() {

        for (int i = 0; i < 50_000; i++) {
            Math.sin(i);
        }
    }
    private static long processScore(ExamResult res) {
        System.out.println(Thread.currentThread().getName());
        heavyCpuLoad();
        return res.getScore();
    }

    public Map<Subject, Double> avgBallAllSubject(boolean isParallel) {
        return isParallel? results.parallelStream().collect(Collectors.groupingByConcurrent(ExamResult::getSubject, Collectors.averagingDouble(Statistic::processScore)))
                :
                results.stream().collect(Collectors.groupingBy(ExamResult::getSubject, Collectors.averagingDouble(Statistic::processScore)));
    }

    public Map<Subject, Long> maxBall(boolean isParallel) {
        return isParallel? results.parallelStream().collect(Collectors.groupingByConcurrent(ExamResult::getSubject, Collectors.reducing(0L, res -> Long.valueOf(processScore(res)), Long::max)))
                :
                results.stream().collect(Collectors.groupingBy(ExamResult::getSubject, Collectors.reducing(0L, res -> Long.valueOf(processScore(res)), Long::max)));
    }

    public Map<Student, Map<Subject, Double>> topStudent(boolean isParallel) {
        Map<Student, Map<Subject, Double>> allMarksAvgStuds = isParallel ?
                results.parallelStream().
                        collect(Collectors.groupingByConcurrent(ExamResult::getStudent,
                                Collectors.groupingBy(ExamResult::getSubject, Collectors.averagingDouble(Statistic::processScore)))):

                results.stream().
                        collect(Collectors.groupingBy(ExamResult::getStudent,
                                Collectors.groupingBy(ExamResult::getSubject, Collectors.averagingDouble(Statistic::processScore))));

        var stream = isParallel ? allMarksAvgStuds.entrySet().parallelStream() : allMarksAvgStuds.entrySet().stream();

        return stream.
                filter(marks-> marks.getValue().values().stream().
                    allMatch(mark -> mark >= 90)).
                sorted(Comparator.comparing(el->el.getKey().getName())).
                collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(ex,rp)->ex, LinkedHashMap::new));
        }
}
