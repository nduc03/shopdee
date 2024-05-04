package Order;

import Item.Cart;
import Shop.Shop;

public class OrderContent {
    private final Cart cart;
    private final Shop shop;

    private OrderContent(Shop shop, Cart items) {
        this.cart = items;
        this.shop = shop;
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

    public Cart getCart() {
        return cart;
    }
}
