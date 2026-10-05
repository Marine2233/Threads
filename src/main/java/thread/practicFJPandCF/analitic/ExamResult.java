package thread.practicFJPandCF.analitic;

import lombok.Getter;

@Getter
public class ExamResult {
    private final Student student;
    private final Subject subject;
    private final int score;

    public ExamResult(int score, Student student, Subject subject) {
        this.score = score;
        this.student = student;
        this.subject = subject;
    }
}
