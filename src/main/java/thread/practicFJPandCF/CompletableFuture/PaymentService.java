package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Getter
public class PaymentService {
    private ExecutorService service;
    private List<PayInfo>payInfoList;
    private List<User>userList;

    public PaymentService(ExecutorService service,List<User>userList) {
        Random random = new Random();
        this.service = service;
        this.userList = userList;
        payInfoList = new ArrayList<>();
        int count = random.nextInt(1,7);
        userList.forEach(user -> {
            payInfoList.add(new PayInfo(count,user.getId(),count));
        });


    }

    public CompletableFuture<PayInfo>loadPayInfo(ExecutorService service,User use){
        int id = use.getId();
        return new CompletableFuture<PayInfo>().
                completeAsync(()-> payInfoList.stream().
                        filter(payInfo -> payInfo.getUserId() == id).findFirst().orElse(null),service);
    }
}
