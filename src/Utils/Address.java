package Utils;

public record Address(String addressLine, Utils.Address.City city) {
    public enum City {
        HANOI, HCMC, DANANG, HAIPHONG, CANTHO
    }
}
