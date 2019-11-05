package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

public class GoldenCard extends Card{

    public GoldenCard(String name) {
        super(name);
    }

    public boolean executePayment(double cost) {
        if (cost < 0 || cost > this.amount)
            return false;

        amount -= cost;
        amount += cost * 15 / 100;
        return true;
    }
}
