import java.util.List;

public class Customer extends User{
    List<ItemStock> cart;
    List<Order> orders;
    Shop ownedShop;

    public List<ItemStock> getCart() {
        return cart;
    }

    public void setCart(List<ItemStock> cart) {
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
