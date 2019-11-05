package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

import java.util.Objects;

public abstract class Card {
    String name;
    double amount;

    public Card() {
        this.name = "";
        this.amount = 0;
    }

    public Card(String name) {
        this.name = name;
        this.amount = 0;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public boolean feed (double amount) {
        if (amount < 0)
            return false;

        this.amount += amount;
        return true;
    }
    public abstract boolean executePayment(double cost);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return name.equals(card.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
