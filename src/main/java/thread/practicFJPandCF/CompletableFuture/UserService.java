package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
@Getter
public class UserService {
    private List<User> userList ;
    private ExecutorService service;

    public UserService(ExecutorService service,List<User>usList) {
        this.service = service;
        this.userList = usList;
    }

    public  CompletableFuture<User>loadUser(ExecutorService service,int id){
        return new CompletableFuture<User>().completeAsync(()->userList.get(id),service);

    }
}
