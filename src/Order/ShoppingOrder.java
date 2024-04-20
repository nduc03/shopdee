package Order;

import Item.Cart;
import User.Customer;

import java.util.Date;

public class ShoppingOrder extends Order {
    public ShoppingOrder(Customer customer, Date orderedDate, Cart content) {
        super(customer, orderedDate, content);
    }
}
