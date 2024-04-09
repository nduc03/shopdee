import java.util.Date;

public class Voucher {
    private final int id;
    private final String name;
    private final double discount;
    private final Date expirationDate;

    private static int currentId = 1;
    public Voucher(String name, double discount, Date expirationDate) {
        this.id = currentId++;
        this.name = name;
        this.discount = discount;
        this.expirationDate = expirationDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDiscount() {
        return discount;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
