package User;

import Order.Order;
import Order.OrderState;
import Utils.Address;

public class Shipper extends User {
    public Shipper(String username, String password, String name, String phone, Address address) {
        super(username, password, name, phone, address, UserRole.Shipper);
    }

    public void takesOrder(Order order) {
        if (!order.getOrderState().equals(OrderState.SHOP_ACCEPTED)
                || !order.getOrderState().equals((OrderState.AT_WAREHOUSE))
                || !order.getLocation().city().equals(this.getAddress().city())
        ) throw new IllegalArgumentException("Invalid order state or city.");

        order.setLocation(new Address("Shipping", order.getShop().getAddress().city()));
        order.setOrderState(OrderState.SHIPPING);
        order.setShipper(this);
    }

    public void finishesOrder(Order order, double payAmount) {
        if (!order.getOrderState().equals(OrderState.SHIPPING))
            throw new IllegalArgumentException("Invalid order state");
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
