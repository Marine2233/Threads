package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;
public class Order {

    @Getter
    private String name;
    @Getter
    private DeLiveryInfo info;
    public Order(int id,DeLiveryInfo info){
        name = "Order-"+id;
        this.info = info;

    }
    @Override
    public String toString(){
        return String.format("%s; %s",name,info);
    }
}
