package Order;

import Shop.Shop;
import User.Customer;
import User.Shipper;
import Utils.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Order {
    private final int id;
    private Date orderedDate;
    @NotNull
    private final Customer customer;
    @NotNull
    private final OrderContent content;
    @NotNull
    private final Shop shop;
    @NotNull
    private OrderState orderState;
    private Shipper shipper;
    @NotNull
    private Address location;

    private static int currentId = 10000; // from 10_000 to 19_999

    @JsonCreator
    private Order(
            @JsonProperty("id") int id,
            @JsonProperty("orderedDate") Date orderedDate,
            @JsonProperty("customer") @NotNull Customer customer,
            @JsonProperty("content") @NotNull OrderContent content,
            @JsonProperty("shop") @NotNull Shop shop,
            @JsonProperty("orderState") @NotNull OrderState orderState,
            @JsonProperty("shipper") Shipper shipper,
            @JsonProperty("location") @NotNull Address location
    ) {
        this.id = id;
        this.orderedDate = orderedDate;
        this.customer = customer;
        this.content = content;
        this.shop = shop;
        this.orderState = orderState;
        this.shipper = shipper;
        this.location = location;
        if (id > currentId) {
            currentId = id;
        }
    }

    // an order contents must come from only one shop, using OrderContent to ensure this
    public Order(@NotNull Customer customer, Date orderedDate, @NotNull OrderContent content) {
        if ((currentId - 19_999) % 100_000 == 0) {
            currentId += 100_000 - 9999;
        }
        this.id = ++currentId;
        this.customer = customer;
        this.orderedDate = orderedDate;
        this.content = content;
        this.shop = content.getShop();
        this.orderState = OrderState.CREATED;
        this.location = shop.getAddress();
        shipper = null;
    }

    public int getId() {
        return id;
    }

    public @NotNull Customer getCustomer() {
        return customer;
    }

    public Address getCustomerAddress() {
        return customer.getAddress();
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public @NotNull OrderContent getContent() {
        return content;
    }

    public @NotNull Shop getShop() {
        return shop;
    }

    public @NotNull OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(@NotNull OrderState orderState) {
        this.orderState = orderState;
    }

    public @NotNull Address getLocation() {
        return location;
    }

    public void setLocation(@NotNull Address location) {
        this.location = location;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order-------------" +
                "\nid=" + id +
                "\norderedDate=" + orderedDate.toString() +
                "\ncustomer=" + customer.getName() +
                "\norderState=" + orderState +
                "\ncontent:\n" + content +
                '\n';
    }
}
