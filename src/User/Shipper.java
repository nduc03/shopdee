package User;

import Utils.Address;


public class Shipper extends User {
    public Shipper(String username, String password, String name, String phone, Address address) {
        super(username, password, name, phone, address, UserRole.Shipper);
    }
}
