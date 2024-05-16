package Order;

import Item.Cart;
import Shop.Shop;
import User.Customer;
import User.Shipper;
import Utils.Address;

import java.util.Date;
import java.util.Objects;

public class Order {
    private final int id;
    private Date orderedDate;
    private final Customer customer;
    private final OrderContent content;
    private final Shop shop;
    private OrderState orderState;
    private Shipper shipper;
    private Address location;

    private static int currentId = 1;

    // an order contents must come from only one shop, using OrderContent to ensure this
    public Order(Customer customer, Date orderedDate, OrderContent content) {
        this.id = currentId++;
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

    public Customer getCustomer() {
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

    public Cart getContent() {
        return content;
    }

    public Shop getShop() {
        return shop;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
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
        return "Order{" +
                "\nid=" + id +
                "\norderedDate=" + orderedDate.toString() +
                "\ncustomer=" + customer +
                "\norderState=" + orderState.toString() +
                "\ncontent=" + content.toString() +
                '\n';
    }
}
