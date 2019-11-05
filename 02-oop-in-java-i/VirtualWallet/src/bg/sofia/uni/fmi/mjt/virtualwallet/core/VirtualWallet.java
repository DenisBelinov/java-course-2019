package bg.sofia.uni.fmi.mjt.virtualwallet.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import bg.sofia.uni.fmi.mjt.virtualwallet.core.card.Card;
import bg.sofia.uni.fmi.mjt.virtualwallet.core.ledger.PaymentLedger;
import bg.sofia.uni.fmi.mjt.virtualwallet.core.payment.PaymentInfo;


public class VirtualWallet implements VirtualWalletAPI {

    private static final int MAX_CARD_COUNT = 5;

    private List<Card> registeredCards = new ArrayList<>();
    private PaymentLedger ledger = new PaymentLedger();

    public boolean registerCard(Card card) {
        if (registeredCards.size() >= MAX_CARD_COUNT || registeredCards.contains(card))
            return false;
        return validateAndRegisterCard(card);
    }

    public boolean executePayment(Card card, PaymentInfo paymentInfo) {
        if (registeredCards.contains(card) && paymentInfo != null)
            // I will use && because logPayment is always true, if that changes, add an if.
            return card.executePayment(paymentInfo.getCost()) && ledger.logPayment(card.getName(), LocalDateTime.now(), paymentInfo);

        return false;
    }

    public boolean feed(Card card, double amount) {
        if (registeredCards.contains(card))
            return card.feed(amount);

        return false;
    }

    public Card getCardByName(String name) {
        for (Card c : registeredCards) {
            if (c.getName().equals(name))
                return c;
        }

        return null;
    }

    public int getTotalNumberOfCards() {
        return registeredCards.size();
    }

    /**
     * util function to validate if the card is not null and has a name
     */
    private boolean validateAndRegisterCard(Card card) {
        if (card == null || card.getName() == null || card.getName().equals(""))
            return false;

        registeredCards.add(card);
        return true;
    }
}
