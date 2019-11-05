package bg.sofia.uni.fmi.mjt.virtualwallet.core.ledger;

import bg.sofia.uni.fmi.mjt.virtualwallet.core.payment.PaymentInfo;

import java.time.LocalDateTime;

/**
 * A class to store a single payment.
 * Used by @PaymentLedger
 */
public class PaymentLogEntry {
    private String cardName;
    private LocalDateTime date;
    private PaymentInfo paymentInfo;

    public PaymentLogEntry(String cardName, LocalDateTime date, PaymentInfo paymentInfo) {
        this.cardName = cardName;
        this.date = date;
        this.paymentInfo = paymentInfo;
    }

    public String getCardName() {
        return cardName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }
}
