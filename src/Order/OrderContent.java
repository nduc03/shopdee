package Order;

import Item.Cart;
import Shop.Shop;

// Cart filtered by shop
public class OrderContent extends Cart {
    private final Shop shop;

    private OrderContent(Shop shop, Cart items) {
        super();
        this.shop = shop;
        setItems(items.getItems());
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
