import java.util.*;
import java.util.stream.Stream;

public class ShopdeeSystem {
    private static Hashtable<String, Customer> customers;
    private static Hashtable<String, Shipper> shippers;
    private static HashSet<Payment> holdingPayments;
    private static List<Order> shipping;

    private ShopdeeSystem() {
        // TODO: Đọc file -> khởi tạo mọi thứ từ file
        holdingPayments = new HashSet<>();
        shipping = new ArrayList<>();
    }

    private static final class ShopdeeSystemHolder {
        private static final ShopdeeSystem instance = new ShopdeeSystem();
    }

    public static ShopdeeSystem getInstance() {
        return ShopdeeSystemHolder.instance;
    }


    /**
     * Should be invoked every fixed amount of time.
     * Check the holdingPayments and shipping status, remove or send if appropriate
     */
    public void onPeriodicCheck() {
    }

    public boolean createOrder(Customer customer, Payment payment) {
        if (customer == null || payment == null) return false;
        shipping.add(new Order(
                customer.getAddress(),
                new Date(),
                customer.releaseCart()
        ));
        return true;
    }

    public boolean shipperTakeOrder(Shipper shipper, Order order) {
        if (shipper == null || order == null) return false;
        if (shipper.getOrder() != null || order.getShipper() != null) return false;

        shipper.setOrder(order);
        order.setShipper(shipper);
        return true;
    }

    public boolean userConfirmOrder(Customer customer, Order order) {
        if (customer == null || order == null) return false;
        customer.removeOrder(order);
        shipping.remove(order);
        List<Payment> thisCustomerPayments = holdingPayments.stream()
                .filter(payment -> payment.getSender().equals(customer)).toList();

        for (ItemStock itemStock : order.getContent()) {
            Customer seller = itemStock.getShop().getShopOwner();
            thisCustomerPayments.stream()
                    .filter(payment -> payment.getReceiver().equals(seller))
                    .forEach(payment -> {
                        seller.addBilledPayment(payment);
                        holdingPayments.remove(payment);
                    });
        }
        return true;
    }

    public boolean registerCustomer(String username, String password) {
        if (customers.containsKey(username)) {
            return false;
        }
        customers.put(username, new Customer(/*TODO: Placeholder*/));
        return true;
    }

    public Optional<User> authorize(String username, String password) {
        return null;
    }

    public Optional<ItemStock> searchItem(String itemName) {
        return null;
    }
}
