package Order;

import Item.Cart;
import Item.CartItem;
import Item.Item;
import Shop.Shop;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Hashtable;
import java.util.Map;

// Cart filtered by shop
public class OrderContent {
    private final Shop shop;
    private final double totalPrice;
    private final Hashtable<Item, Integer> items;

    @JsonCreator
    private OrderContent(
            @JsonProperty("shop") Shop shop,
            @JsonProperty("totalPrice") double totalPrice,
            @JsonProperty("items") Hashtable<Item, Integer> items
    ) {
        this.shop = shop;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    private OrderContent(Shop shop, Cart cart) {
        super();
        this.shop = shop;
        totalPrice = cart.getTotalPrice();
        items = new Hashtable<>();
        for (CartItem cartItem : cart.getItems()) {
            Item thisItem = cartItem.getItemStock().getItem();
            if (items.containsKey(thisItem)) {
                items.put(cartItem.getItemStock().getItem(),
                        items.get(thisItem) + cartItem.getQuantity());
                continue;
            }
            items.put(thisItem, cartItem.getQuantity());
        }
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public Hashtable<Item, Integer> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "OrderContent: " +
                "\nshop=" + shop.getName() +
                "\ntotalPrice: " + totalPrice +
                "\nitems:\n" + itemsToString();
    }

    private String itemsToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Item, Integer> item : items.entrySet()) {
            sb.append(String.format("%s - %d\n", item.getKey().getName(), item.getValue()));
        }
        return sb.toString();
    }
}
