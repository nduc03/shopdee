package Item;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Cart {
    private final HashSet<CartItem> items;

    public Cart() {
        items = new HashSet<>();
    }

    public Cart(Collection<CartItem> cartItems) {
        items = new HashSet<>();
        items.addAll(cartItems);
    }

    public void addToCart(CartItem cartItem) {
        if (cartItem != null && items.contains(cartItem)) {
            items.add(cartItem);
        }
    }

    public void addToCart(int cartId, int quantity) {
        if (quantity < 0) return;
        this.items.stream()
                .filter(cartItem -> cartItem.getId() == cartId).findFirst()
                .ifPresent(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity));
    }

    public void removeFromCart(int cartId, int quantity) {
        if (quantity < 0) return;
        this.items.stream().filter(cart -> cart.getId() == cartId).findFirst().ifPresent(cartItem -> {
            if (cartItem.getQuantity() <= quantity) {
                this.items.remove(cartItem);
            }
            else {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
                if (cartItem.getQuantity() <= 0) {
                    this.items.remove(cartItem);
                }
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

    public boolean existsInCart(int cartId) {
        return items.stream().filter(item -> item.getId() == cartId).findFirst().orElse(null) != null;
    }

    @Override
    public String toString() {
        return items.stream().map(item -> item.toString()).collect(Collectors.joining("\n"));
    }
}


