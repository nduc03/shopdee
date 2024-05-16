package User;

import Item.Cart;
import Item.CartItem;
import Item.ItemStock;
import Order.Order;
import Order.OrderState;
import Shop.Shop;
import Utils.Address;

public class Customer extends User {
    private Cart cart;
    private Shop ownedShop;

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

    public Cart buy() {
        Cart oldCart = this.cart;
        if (oldCart.getTotalPrice() > getBalance())
            throw new Error("Not enough balance to buy");
        this.cart = new Cart();
        decreaseBalance(oldCart.getTotalPrice());
        return oldCart;
    }

    public boolean confirmOrder(Order order) {
        if (!order.getCustomer().equals(this)) return false;

        order.setOrderState(OrderState.CUSTOMER_CONFIRMED);
        return true;
    }
}
