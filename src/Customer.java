import java.util.Hashtable;
import java.util.List;

public class Customer extends User{
    Hashtable<Integer, Integer> cart;
    List<Order> orders;
    Shop ownedShop;

    public Hashtable<Integer, Integer> getCart() {
        return cart;
    }

    public void setCart(Hashtable<Integer, Integer> cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Shop getOwnedShop() {
        return ownedShop;
    }

    public void setOwnedShop(Shop ownedShop) {
        this.ownedShop = ownedShop;
    }
}
