public class Shipper extends User{
    private Order order;
    public Shipper() {

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
