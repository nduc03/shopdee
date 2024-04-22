package Order;

import Item.Cart;
import User.Customer;
import Utils.Address;

import java.util.Date;
import java.util.Objects;

public abstract class Order {
    private final int id;
    private Date orderedDate;
    private final Customer customer;
    private final Cart content;

    private static int currentId = 1;

    public Order(Customer customer, Date orderedDate, Cart content) {
        this.id = currentId++;
        this.customer = customer;
        this.orderedDate = orderedDate;
        this.content = content;
    }
    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Address getAddress() {
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
}
