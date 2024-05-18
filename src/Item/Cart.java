package Item;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.CLASS
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = OrderContent.class, name = "OrderContent")
//})
public class Cart {
    @NotNull
    private final HashSet<CartItem> items;

//    @JsonCreator
//    private Cart(@JsonProperty("items") HashSet<CartItem> items) {
//        this.items = items;
//    }

    public Cart() {
        items = new HashSet<>();
    }

    public Cart(Collection<CartItem> cartItems) {
        items = new HashSet<>();
        items.addAll(cartItems);
    }

    public void addToCart(CartItem cartItem) {
        if (cartItem != null) {
            if (items.contains(cartItem)) {
                this.items.stream()
                        .filter(item -> item.getId() == cartItem.getId()).findFirst()
                        .ifPresent(item -> item.setQuantity(item.getQuantity() + cartItem.getQuantity()));
            } else
                items.add(cartItem);
        }
    }

//    public void addToCart(int cartId, int quantity) {
//        if (quantity < 0) return;
//        this.items.stream()
//                .filter(cartItem -> cartItem.getId() == cartId).findFirst()
//                .ifPresent(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity));
//    }

    public void removeFromCart(int cartId, int quantity) {
        if (quantity < 0) return;
        this.items.stream().filter(cart -> cart.getId() == cartId).findFirst().ifPresent(cartItem -> {
            if (cartItem.getQuantity() <= quantity) {
                this.items.remove(cartItem);
            } else {
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

    //    @JsonIgnore
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public @NotNull HashSet<CartItem> getItems() {
        return items;
    }

    public boolean existsInCart(int cartId) {
        return items.stream().filter(item -> item.getId() == cartId).findFirst().orElse(null) != null;
    }
//
//    protected void setItems(Collection<CartItem> items) {
//        this.items.addAll(items);
//    }

    //    @JsonIgnore
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        return items.stream().map(item -> item.toString()).collect(Collectors.joining("\n"));
    }
}


