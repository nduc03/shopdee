import java.util.ArrayList;
import java.util.List;

public class Customer extends User{
    private List<ItemStock> cart;
    private Shop ownedShop;

    public List<ItemStock> getCart() {
        return cart;
    }

    public void setCart(List<ItemStock> cart) {
        this.cart = cart;
    }

    public Shop getOwnedShop() {
        return ownedShop;
    }

    public void setOwnedShop(Shop ownedShop) {
        this.ownedShop = ownedShop;
    }

    public void addToCart(ItemStock item){
        if (item != null) this.cart.add(item);
    }

    public void removeFromCart(ItemStock item){
        if (item != null) this.cart.remove(item);
    }

    public List<ItemStock> releaseCart(){
        List<ItemStock> cartReleased = this.cart;
        this.cart = new ArrayList<>();
        return cartReleased;
    }
}
