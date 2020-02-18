package bg.sofia.uni.fmi.mjt.splitwise.backend.data;

import java.util.Objects;

/**
 * A class representing the relationship between two users.
 *
 * A relationship denotes what is owed by whom between those two users.
 *
 * The amount can also be negative, meaning the direction of the Debt is reversed.
 *
 * fromUsername = denis, toUsername = ivan, amount = 10 => denis OWES ivan 10 lv
 * fromUsername = denis, toUsername = ivan, amount = -10 => ivan OWES denis 10 lv
 *
 * TODO: add reason: ammount in order to have group debts
 */
public class Debt {
    private final String fromUsername;
    private final String toUsername;

    private Double amount;

    public Debt(String fromUsername, String toUsername, Double amount) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.amount = amount;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Debt)) return false;
        Debt debt = (Debt) o;
        return getFromUsername().equals(debt.getFromUsername()) &&
                getToUsername().equals(debt.getToUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromUsername(), getToUsername());
    }
}
