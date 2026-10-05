package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Getter
public class DeliveryService {
    private List<DeLiveryInfo> deLiverysInfo;
    private UserService service;
    private ExecutorService ex;

    public DeliveryService(UserService service,ExecutorService serviceEx){
        this.service = service;
        deLiverysInfo = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            User user = service.getUserList().get(i);
            deLiverysInfo.add(new DeLiveryInfo(user.getId()));
            ex=serviceEx;
        }
    }
    public CompletableFuture<DeLiveryInfo>loadDelivery(ExecutorService service,User user){
        return CompletableFuture.supplyAsync(()->
                deLiverysInfo.stream().filter(deL-> deL.getUserId() == user.getId()).findAny().orElse(null),service);
    }
}
