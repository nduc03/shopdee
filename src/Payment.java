import java.util.Date;
import java.util.Objects;

public class Payment {
    private final int id;
    private final double amount;
    private final Date date;
    private final User sender;
    private final User receiver;
    private final Order order;

    private static int currentId = 1;

    public Payment(double amount, Date date, User sender, User receiver, Order order) {
        this.id = currentId++;
        this.amount = amount;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        return id == payment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
