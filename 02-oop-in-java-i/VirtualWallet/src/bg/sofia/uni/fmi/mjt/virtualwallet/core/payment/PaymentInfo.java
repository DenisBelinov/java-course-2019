package bg.sofia.uni.fmi.mjt.virtualwallet.core.payment;

public class PaymentInfo {
    String reason;
    String location;
    double cost;

    public PaymentInfo(String reason, String location, double cost) {
        this.reason = reason;
        this.location = location;
        this.cost = cost;
    }

    public String getLocation() {
        return location;
    }

    public double getCost() {
        return cost;
    }

    public String getReason() {
        return reason;
    }

}
