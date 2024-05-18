package Shop;

import Item.ItemStock;
import Order.Order;
import Order.OrderState;
import User.Customer;
import Utils.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id"
//)
public class Shop {
//    private final int id;
    @NotNull
    private String name;
    @JsonManagedReference
    private final List<ItemStock> stock;
    private double revenue;
    @NotNull
    private Address address;

//    private static int currentId = 40_000; // range 40_000 - 49_999

    @JsonCreator
    private Shop(
//            @JsonProperty("id") int id,
            @JsonProperty("name") @NotNull String name,
            @JsonProperty("stock") List<ItemStock> stock,
            @JsonProperty("revenue") double revenue,
            @JsonProperty("address") @NotNull Address address
    ){
//        this.id = id;
        this.name = name;
        this.stock = stock;
        this.revenue = revenue;
        this.address = address;
        for (ItemStock itemStock : stock) {
            if (itemStock.getShop() == null)
                itemStock.setShop(this);
        }
//        if (id > currentId) currentId = id;
    }

    public Shop(@NotNull String name, @NotNull Address address) {
//        id = ++currentId;
//        if ((currentId - 49_999) % 100_000 == 0) {
//            currentId += 100_000 - 9999;
//        }
        this.name = name;
        this.revenue = 0.0;
        this.address = address;
        stock = new ArrayList<>();
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public List<ItemStock> getStock() {
        return stock;
    }

//    public int getId() {
//        return id;
//    }

    public double getRevenue() {
        return revenue;
    }

    public @NotNull Address getAddress() {
        return address;
    }

    public void setAddress(@NotNull Address address) {
        this.address = address;
    }

    public void setRevenue(double revenue) {
        this.revenue = Math.max(revenue, 0.0);
    }

    public void increaseRevenue(double amount) {
        revenue += amount;
    }

    public void withdraw(@NotNull Customer customer, double amount) {
        if (customer.getOwnedShop().equals(this)) { // check customer to ensure only shop owner can withdraw
            revenue -= amount;
            customer.addBalance(amount);
        }
    }

    public void addItem(@NotNull ItemStock itemStock) {
//        for (ItemStock item : stock) {
//            if (item.getId() == itemStock.getId()) {
//                item.setQuantity(item.getQuantity() + itemStock.getQuantity());
//                return;
//            }
//        }

        stock.add(itemStock);
    }

    public boolean removeItem(int itemId) {
        for (int i = 0; i < stock.size(); i++) {
            if (stock.get(i).getId() == itemId) {
                stock.remove(i);
                return true;
            }
        }
        return false;
    }

//    public Order makeOrder(Customer customer, OrderContent orderContent) {
//        if (customer == null || orderContent == null) throw new IllegalArgumentException("Null argument");
//        if (!orderContent.getShop().equals(this)) throw new IllegalArgumentException("Invalid shop");
//        orderContent.getItems().forEach(cartItem -> {
//            ItemStock itemStock = cartItem.getItemStock();
//            itemStock.setQuantity(itemStock.getQuantity() - cartItem.getQuantity());
//        });
//        return new Order(customer, new Date(), orderContent);
//    }

    public void acceptOrder(Order order) {
        if (order != null && order.getShop() == this && order.getOrderState() == OrderState.CREATED) {
            order.setOrderState(OrderState.SHOP_ACCEPTED);
        }
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Shop shop)) return false;
//        return id == shop.id;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(id);
//    }


    @Override
    public String toString() {
        return "Shop: " +
                "\nname='" + name + '\'' +
                "\naddress=" + address +
                "\nItems are selling:\n" + stockToString() + "-------------------------\n";
    }

    private String stockToString() {
        StringBuilder sb = new StringBuilder();
        for (ItemStock itemStock : stock) {
            sb.append(itemStock.toString()).append("\n");
        }
        return sb.toString();
    }
}
