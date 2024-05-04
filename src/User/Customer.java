package User;

import Item.Cart;
import Item.CartItem;
import Item.ItemStock;
import Shop.Shop;
import Order.Order;
import Utils.Address;

import java.util.List;

public class Customer extends User {
    private Cart cart;
    private Shop ownedShop;
    private List<Order> orderList;

    public Customer(String username, String password, String name, String phone, Address address) {
        super(username, password, name, phone, address, UserRole.Customer);
        this.cart = new Cart();
        this.ownedShop = null;
    }

    public Cart getCart() {
        return cart;
    }

    public Shop getOwnedShop() {
        return ownedShop;
    }

    public void setOwnedShop(Shop ownedShop) {
        this.ownedShop = ownedShop;
    }

    public void addToCart(ItemStock item, int quantity) {
        if (item == null) return;

        this.cart.addToCart(new CartItem(quantity, item));
    }

    public void addToCart(int cartId, int quantity) {
        this.cart.addToCart(cartId, quantity);
    }

    public void removeFromCart(int cartId, int quantity) {
        this.cart.removeFromCart(cartId, quantity);
    }

    public void removeFromCart(int cartId) {
        this.cart.removeFromCart(cartId);
    }

    public Cart releaseCart() {
        Cart cartReleased = this.cart;
        this.cart = new Cart();
        return cartReleased;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
