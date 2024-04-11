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

    public void addBalance(double amount) {
        balance += amount;
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
