import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private final int id;
    private Date orderedDate;
    private final Customer customer;
    private Shipper shipper;
    private boolean shipped;
    private final List<ItemStock> content;

    private static int currentId = 1;

    public Order(Customer customer, Date orderedDate, List<ItemStock> content) {
        this.id = currentId++;
        this.customer = customer;
        this.orderedDate = orderedDate;
        this.shipper = null;
        this.shipped = false;
        this.content = content;
    }

    public void shipped() {
        this.shipped = true;
        this.shipper = null;
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

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        if (shipper == null) return;
        this.shipper = shipper;
    }

    public boolean getShipped() {
        return shipped;
    }

    public List<ItemStock> getContent() {
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
