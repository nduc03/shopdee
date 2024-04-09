import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private double balance;
    private String phone;
    private Address address;
    private UserRole role;
    private List<Payment> billedPayments;

    protected static int currentId = 1;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public UserRole getRole() {
        return role;
    }

    /**
     * Convert all the payment bills this user have to balance
     */
    public void consumeBilledPayment() {
        billedPayments.forEach(payment -> balance += payment.getAmount());
        billedPayments = new ArrayList<>();
    }

    /**
     * If payment has correct receiver, the payment will be added to this user and return true.
     * Otherwise, return false
     * @param payment billed payment
     * @return true if successfully added the payment to this user, false if payment is null or wrong receiver
     */
    public boolean addBilledPayment(Payment payment) {
        if (payment != null && payment.getReceiver().getId() == id) {
            billedPayments.add(payment);
            return true;
        }
        return false;
    }
    
    public void decreaseBalance(double amount) {
        balance -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id == user.id && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
