package User;

import Utils.Address;

import java.util.Objects;

public abstract class User {
    private final int id;
    private final String username;
    private String password;
    private String name;
    private double balance;
    private String phone;
    private Address address;
    private final UserRole role;

    protected static int currentId = 1;

    User(String username, String password, String name, String phone, Address address, UserRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.balance = 0.0;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.id = currentId++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
