package Item;

import java.util.HashSet;

public class Cart {
    private final HashSet<CartItem> items;

    public Cart() {
        items = new HashSet<>();
    }

    public void addToCart(CartItem cartItem) {
        if (cartItem != null && items.contains(cartItem)) {
            items.add(cartItem);
        }
    }

    public void addToCart(int cartId, int quantity) {
        this.items.stream()
                .filter(cartItem -> cartItem.getId() == cartId).findFirst()
                .ifPresent(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity));
    }

    public void removeFromCart(int cartId, int quantity) {
        this.items.stream().filter(cart -> cart.getId() == cartId).findFirst().ifPresent(cartItem -> {
            if (cartItem.getQuantity() <= quantity) {
                this.items.remove(cartItem);
            }
            else {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
            }
        });
    }

    public void removeFromCart(int cartId) {
        this.items.stream().filter(cart -> cart.getId() == cartId).findFirst().ifPresent(this.items::remove);
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public HashSet<CartItem> getItems() {
        return items;
    }
}


