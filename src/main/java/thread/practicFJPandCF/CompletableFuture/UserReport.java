package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Getter
public class UserReport {
    private UserService userService;
    private PaymentService payService;
    private OrderService orderService;
    private DeliveryService deliveryService;
    private ExecutorService exService;
    private User user;
    private List<Order> orders;
    private DeLiveryInfo deLiveryInfo;
    private PayInfo info;

    public UserReport(User user,List<Order>orders,DeLiveryInfo info,PayInfo payInfo){
        this.user = user;
        this.orders = orders;
        this.deLiveryInfo = info;
        this.info = payInfo;
    }

    public UserReport(ExecutorService service,DeliveryService deliveryService, OrderService orderService, PaymentService payService, UserService userService) {
        this.deliveryService = deliveryService;
        this.orderService = orderService;
        this.payService = payService;
        this.userService = userService;
        this.exService = service;
    }

    public CompletableFuture<UserReport> report(int userId){

        return userService.loadUser(exService,userId).thenCompose(user->{

            CompletableFuture<List<Order>>futureOrder = orderService.loadOrders(exService,user);

            CompletableFuture<PayInfo>payFuture = payService.loadPayInfo(exService,user);

            CompletableFuture<DeLiveryInfo>deliveryInfo = deliveryService.loadDelivery(exService,user);

            CompletableFuture<PayAndDelivery> payDelFuture = payFuture.thenCombine(deliveryInfo,(p,d)->
                    new PayAndDelivery(p,d));

            return CompletableFuture.allOf(futureOrder,payDelFuture).
                    thenApplyAsync(v->{

                 PayAndDelivery pad = payDelFuture.join();
                 List<Order>orders1 = futureOrder.join();

                 return new UserReport(user,orders1,pad.deliveryInfo,pad.payInfo);
            },exService);

        }).exceptionally(throwable -> {
            System.err.println("Произошла ошибка при генерации отчета: " + throwable.getMessage());
            return null;

        }).whenComplete((ur,throwable)->{
            if (throwable != null){
                System.out.println("Цепочка завершилась с критической ошибкой.");
            } else {
                System.out.println("Генерация отчета успешно завершена для пользователя: "
                        + ur.getUser().getFullName());
            }
        });
    }

    @Override
    public String toString(){
        return String.format("%s orders: %s; pay info: %s; delivery: %s",user,orders.size(),info,deLiveryInfo);
    }

    private static class PayAndDelivery {
        final PayInfo payInfo;
        final DeLiveryInfo deliveryInfo;
        PayAndDelivery(PayInfo payInfo, DeLiveryInfo deliveryInfo) {
            this.payInfo = payInfo;
            this.deliveryInfo = deliveryInfo;
        }
    }
}
