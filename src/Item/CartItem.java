package Item;

import java.util.Objects;

public class CartItem {
    private final int id;
    private int quantity;
    private final ItemStock itemStock;

    private static int currentId = 0;

    public CartItem(int quantity, ItemStock itemStock) {
        id = ++currentId;
        if (quantity <= 0 || quantity > itemStock.getQuantity()) {
            throw new IllegalArgumentException("Invalid quantity when creating a cart item");
        }
        this.quantity = quantity;
        this.itemStock = itemStock;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemStock getItemStock() {
        return itemStock;
    }

    public double getTotalPrice() {
        return itemStock.getPrice() * quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem cartItem)) return false;
        return id == cartItem.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
