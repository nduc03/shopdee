import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private final int id;
    private Address address;
    private Date orderedDate;
    private Shipper shipper;
    private final List<ItemStock> content;

    private static int currentId = 1;

    public Order(Address address, Date orderedDate, List<ItemStock> content) {
        this.id = currentId++;
        this.address = address;
        this.orderedDate = orderedDate;
        this.shipper = null;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
        this.shipper = shipper;
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
