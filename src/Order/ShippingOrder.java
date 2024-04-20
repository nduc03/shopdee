package Order;

import Item.Cart;
import User.Customer;
import User.Shipper;

import java.util.Date;

public class ShippingOrder extends Order {
    private Shipper shipper;
    private boolean shipped;

    public ShippingOrder(Customer customer, Date orderedDate, Cart content) {
        super(customer, orderedDate, content);
        shipped = false;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        if (shipper == null) return;
        this.shipper = shipper;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void shipped() {
        this.shipped = true;
    }
}
