package User;

import Item.Cart;
import Item.CartItem;
import Item.ItemStock;
import Order.Order;
import Order.OrderState;
import Shop.Shop;
import Utils.Address;
import com.fasterxml.jackson.annotation.*;
import org.jetbrains.annotations.NotNull;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Customer extends User {
    @JsonIgnore
    @NotNull
    private Cart cart;
    private Shop ownedShop;

    @JsonCreator
    private Customer(
            @JsonProperty("id") int id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("balance") double balance,
            @JsonProperty("phone") String phone,
            @JsonProperty("address") Address address,
            @JsonProperty("cart") @NotNull Cart cart,
            @JsonProperty("ownedShop") Shop ownedShop
    ) {
        super(id, username, password, name, balance, phone, address, UserRole.Customer);
        this.cart = cart;
        this.ownedShop = ownedShop;
    }

    public Customer(String username, String password, String name, String phone, Address address) {
        super(username, password, name, phone, address, UserRole.Customer);
        this.cart = new Cart();
        this.ownedShop = null;
    }

    public @NotNull Cart getCart() {
        return cart;
    }

    public Shop getOwnedShop() {
        return ownedShop;
    }

    public void setOwnedShop(Shop ownedShop) {
        this.ownedShop = ownedShop;
    }

    public boolean addToCart(ItemStock item, int quantity) {
        if (item == null) return false;

        this.cart.addToCart(new CartItem(quantity, item));
        return true;
    }

//    public void addToCart(int cartId, int quantity) {
//        this.cart.addToCart(cartId, quantity);
//    }

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
