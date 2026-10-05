package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;

@Getter
public class DeLiveryInfo {
    private String info;
    private int userId;
    public DeLiveryInfo(int userId){
        this.userId = userId;
        info = "Delivery-"+userId;
    }

    @Override
    public String toString(){
        return String.format("%s; user id: %s",info,userId);
    }
}
