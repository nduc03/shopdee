package User;

import Utils.Address;
import Utils.Utils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id"
//)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Customer.class, name = "Customer"),
        @JsonSubTypes.Type(value = Shipper.class, name = "Shipper"),
})
public abstract class User {
    private final int id;
    @NotNull
    private final String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    private double balance;
    @NotNull
    private String phone;
    @NotNull
    private Address address;
    @NotNull
    private final UserRole role;

    protected static int currentId = 0; // id range from 1 - 9999, 100_000 -> 109_999, ...

    // Constructor for deserialize json in child classes
    User(
            int id,
            String username,
            String password,
            String name,
            double balance,
            String phone,
            Address address,
            UserRole role
    ) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.balance = balance;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.id = id;
        if (id > currentId) {
            currentId = id;
        }
    }

    // Normal constructor
    User(
            @NotNull String username,
            @NotNull String password,
            @NotNull String name,
            @NotNull String phone,
            @NotNull Address address,
            @NotNull UserRole role
    ) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.balance = 0.0;
        this.phone = phone;
        this.address = address;
        this.role = role;
        if ((currentId + 1) % 10000 == 0) {
            currentId += 100_000 - 9999;
        }
        this.id = ++currentId;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public @NotNull String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        this.phone = phone;
    }

    public @NotNull Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        this.address = address;
    }

    public @NotNull UserRole getRole() {
        return role;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    protected void decreaseBalance(double amount) {
        balance -= amount;
    }

    public double withdraw(double amount) {
        double possibleAmount = Utils.clamp(amount, 0, amount);
        balance -= possibleAmount;
        return possibleAmount;
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

    @Override
    public String toString() {
        return name + "'s info:\n" +
                "Username: '" + username + '\'' +
                "\nBalance: " + balance +
                "\nPhone: '" + phone + '\'' +
                "\nAddress: " + address;
    }
}
