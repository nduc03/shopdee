package Order;

import Item.Cart;
import Item.CartItem;
import Shop.Shop;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

// Cart filtered by shop
public class OrderContent extends Cart {
    private final Shop shop;

    @JsonCreator
    private OrderContent(
            @JsonProperty("items") HashSet<CartItem> items,
            @JsonProperty("shop") Shop shop
    ) {
        super(items);
        this.shop = shop;
    }

    private OrderContent(Shop shop, Cart cart) {
        super();
        this.shop = shop;
        setItems(cart.getItems());
    }

    public static OrderContent filterFromCustomerCart(Shop shop, Cart customerCart) {
        Cart filteredCart = new Cart(customerCart.getItems().stream()
                .filter(cartItem ->
                        cartItem.getItemStock().getShop().equals(shop)
                ).toList());
        return new OrderContent(shop, filteredCart);
    }

    public Shop getShop() {
        return shop;
    }
}
