package Item;

import Shop.Shop;
import Utils.Utils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemStock {
    private final int id;
    @NotNull
    private final Item item;
    private double price;
    private int quantity;
    @JsonBackReference
    private Shop shop;

    private static int currentId = 30_000; // range: 30_000 -> 39_999

    @JsonCreator
    private ItemStock(
            @JsonProperty("id") int id,
            @JsonProperty("item") @NotNull Item item,
            @JsonProperty("price") double price,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("shop") Shop shop
    ) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.shop = shop;
        this.id = id;
        if (id > currentId) {
            currentId = id;
        }
    }

    public ItemStock(@NotNull Item item, double price, int quantity, @NotNull Shop shop) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.shop = shop;
        id = ++currentId;
        if ((currentId - 39_999) % 100_000 == 0) {
            currentId += 100_000 - 9999;
        }
    }

    public int getId() {
        return id;
    }

    public @NotNull Item getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    private void setQuantity(int quantity) {
        this.quantity = Utils.clamp(quantity, 0, Integer.MAX_VALUE);
    }

    public boolean buy(int quantity) {
        if (this.quantity < quantity) {
            return false;
        }
        setQuantity(this.quantity - quantity);
        return true;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(@NotNull Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        String shopName = shop == null ? "null" : shop.getName();
        return "ItemStock:" +
                "\nid: " + id +
                "\nitem name: " + item.getName() +
                "\nprice: " + price +
                "\nquantity: " + quantity +
                "\nshop: " + shopName + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStock itemStock)) return false;
        return id == itemStock.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
