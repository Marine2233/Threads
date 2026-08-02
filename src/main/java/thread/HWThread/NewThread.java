package thread.HWThread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@AllArgsConstructor
@ToString
public class NewThread extends Thread{
    private String name;
    private int firstNum;
    private int lastNum;

    @Override
    public void run() {

        if (firstNum < lastNum) {
            for (int i = firstNum; i <= lastNum; i++) {
                System.out.println(name + ": "+i);

            }
        }
    }
}
