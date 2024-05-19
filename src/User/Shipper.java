package User;

import Order.Order;
import Order.OrderState;
import Utils.Address;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.jetbrains.annotations.NotNull;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Shipper extends User {
    @JsonCreator
    private Shipper(
            @JsonProperty("id") int id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("balance") double balance,
            @JsonProperty("phone") String phone,
            @JsonProperty("address") Address address
    ) {
        super(id, username, password, name, balance, phone, address, UserRole.Shipper);
    }

    public Shipper(String username, String password, String name, String phone, Address address) {
        super(username, password, name, phone, address, UserRole.Shipper);
    }

    public void takesOrder(@NotNull Order order) {
        if (!order.getOrderState().equals(OrderState.SHOP_ACCEPTED)
                && !order.getOrderState().equals((OrderState.AT_WAREHOUSE))
        ) throw new IllegalArgumentException("Invalid order state.");

        if (!order.getLocation().city().equals(this.getAddress().city()))
            throw new IllegalArgumentException("Invalid order city.");

        if (order.getShipper() != null && order.getShipper() != this) {
            throw new IllegalArgumentException("Order are shipping, cannot take.");
        }

        order.setLocation(new Address("Shipping", order.getShop().getAddress().city()));
        order.setOrderState(OrderState.SHIPPING);
        order.setShipper(this);
    }

    public void finishesOrder(@NotNull Order order, double payAmount) {
        if (!order.getOrderState().equals(OrderState.SHIPPING))
            throw new IllegalArgumentException("Invalid order state");
        if (order.getShipper() != this) {
            throw new IllegalArgumentException("Order does not belong to this shipper");
        }
        if (order.getCustomerAddress().city().equals(this.getAddress().city())) {
            order.setOrderState(OrderState.DELIVERED);
            order.setLocation(order.getCustomerAddress());
        } else
            shipToWarehouse(order);
        addBalance(payAmount);
        order.setShipper(null);
    }

    // simulate shipper as warehouse that auto ship to customer city
    private void shipToWarehouse(Order order) {
        order.setOrderState(OrderState.AT_WAREHOUSE);
        order.setLocation(new Address("The warehouse", order.getCustomerAddress().city()));
    }
}
