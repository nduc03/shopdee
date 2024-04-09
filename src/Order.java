import java.util.Date;

public class Order {
    private final int id;
    private Address address;
    private Date orderedDate;

    private static int currentId = 1;

    public Order(Address address, Date orderedDate) {
        this.id = currentId++;
        this.address = address;
        this.orderedDate = orderedDate;
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
}
