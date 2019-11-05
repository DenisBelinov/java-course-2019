package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

public class StandardCard extends Card {

    public StandardCard(String name) {
        super(name);
    }

    public boolean executePayment(double cost) {
        if (cost < 0 || cost > this.amount)
            return false;

        amount -= cost;
        return true;
    }
}
