package Utils;

public class Address {
    private final String addressLine;
    private final String street;
    private final String district;
    private final City city;
    private final String province;

    public enum City {
        HANOI, HCMC, DANANG, HAIPHONG, CANTHO
    }

    public Address(String addressLine, String street, String district, City city, String province) {
        this.addressLine = addressLine;
        this.street = street;
        this.district = district;
        this.city = city;
        this.province = province;
    }

    public Address(String addressLine, String street, String district, City city) {
        this(addressLine, street, district, city, null);
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getStreet() {
        return street;
    }

    public String getDistrict() {
        return district;
    }

    public City getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
}
