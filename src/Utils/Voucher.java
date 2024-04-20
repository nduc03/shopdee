package Utils;

import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voucher voucher)) return false;
        return id == voucher.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
