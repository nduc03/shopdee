package Item;

import Shop.Shop;
import com.fasterxml.jackson.annotation.*;
import org.jetbrains.annotations.NotNull;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class ItemStock {
    private final int id;
    @NotNull
    private final Item item;
    private double price;
    private int quantity;
    @NotNull
    private final Shop shop;

    private static int currentId = 30_000; // range: 30_000 -> 39_999

    @JsonCreator
    private ItemStock(
            @JsonProperty("id") int id,
            @JsonProperty("item") @NotNull Item item,
            @JsonProperty("price") double price,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("shop") @NotNull Shop shop
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
        id = currentId++;
        if ((currentId - 30_000) % 100_000 == 0) {
            currentId += 100_000 - 10_000;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public @NotNull Shop getShop() {
        return shop;
    }

    @Override
    public String toString() {
        return "ItemStock:" +
                "\nid: " + id +
                "\nitem name: " + item.getName() +
                "\nprice: " + price +
                "\nquantity: " + quantity +
                "\nshop: " + shop.getName() + '\n';
    }
}
