package Utils;

public record Address(String addressLine, Address.City city) {
    public enum City {
        HANOI, HCMC, DANANG, HAIPHONG, CANTHO
    }
}
