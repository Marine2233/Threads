package thread.practicFJPandCF.CompletableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PointApp {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();

        UserService userService = new UserService(pool,users(10));
        List<User>users = userService.getUserList();
        PaymentService payService = new PaymentService(pool,users);
        OrderService orderService = new OrderService(pool);
        DeliveryService deliveryService = new DeliveryService(userService,pool);

        UserReport report = new UserReport(pool, deliveryService, orderService, payService, userService);

        UserReport result = report.report(7).join();
        System.out.println(result.toString());

        pool.shutdown();

    }
    public static List<User>users(int count){
        List<User>users= new ArrayList<>();

        for (int id = 0;id < 10; id++) {
            users.add(new User("Customer-" + (id),id));

        }
        return users;
    }
}
