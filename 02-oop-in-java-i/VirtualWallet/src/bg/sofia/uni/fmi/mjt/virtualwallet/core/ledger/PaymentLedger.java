package bg.sofia.uni.fmi.mjt.virtualwallet.core.ledger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import bg.sofia.uni.fmi.mjt.virtualwallet.core.payment.PaymentInfo;


public class PaymentLedger {
    private static final int MAX_ENTRIES = 10;

    private int latestEntryIndex = -1;

    private List<PaymentLogEntry> logEntries = new ArrayList<>();

    public List<PaymentLogEntry> getLogEntries() {
        return logEntries;
    }

    public boolean logPayment(String cardName, LocalDateTime date, PaymentInfo paymentInfo) {
        PaymentLogEntry entry = new PaymentLogEntry(cardName, date, paymentInfo);

        return addEntry(entry);
    }

    private boolean addEntry(PaymentLogEntry entry) {
        // this always returns true
        // I will spend time doing try/catch
        if (logEntries.size() < MAX_ENTRIES) {
            logEntries.add(entry);
            latestEntryIndex = logEntries.indexOf(entry);
        } else {
            int newEntryIndex = (latestEntryIndex + 1) % MAX_ENTRIES;
            logEntries.set(newEntryIndex, entry);
            latestEntryIndex = newEntryIndex;

        }

        return true;
    }
}
