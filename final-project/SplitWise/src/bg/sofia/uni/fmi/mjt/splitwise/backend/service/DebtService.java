package bg.sofia.uni.fmi.mjt.splitwise.backend.service;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Debt;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service responsible for holding debts between users.
 */
public class DebtService {
    private Set<Debt> debts = ConcurrentHashMap.newKeySet(); // concurrent set;

    public void addDebt(String fromUser, String toUser, Double amount) {
        Debt debtForUsers = getDebtForUsers(fromUser, toUser);

        if (debtForUsers.getFromUsername().equals(fromUser)) {
            debtForUsers.setAmount(debtForUsers.getAmount() + amount);
        }
        else {
            debtForUsers.setAmount(debtForUsers.getAmount() - amount);
        }
    }

    public void addPayment(String fromUser, String toUser, Double amount) {
        Debt debtForUsers = getDebtForUsers(fromUser, toUser);

        if (debtForUsers.getFromUsername().equals(fromUser)) {
            debtForUsers.setAmount(debtForUsers.getAmount() - amount);
        }
        else {
            debtForUsers.setAmount(debtForUsers.getAmount() + amount);
        }
    }

    public Debt getDebtForUsers(String user1, String user2) {
        Optional<Debt> existingDebt =  debts.stream().filter(d -> (d.getFromUsername().equals(user1) && d.getToUsername().equals(user2) ||
                                                             d.getFromUsername().equals(user2) && d.getToUsername().equals(user1)))
                                                     .findFirst();

        if (existingDebt.isPresent()) {
            return existingDebt.get();
        }

        Debt newDebt = new Debt(user1, user2, 0.);
        debts.add(newDebt);

        return newDebt;
    }

    public Set<Debt> getDebtsForUser(String user) {
        return debts.stream().filter(d -> (d.getFromUsername().equals(user) || d.getToUsername().equals(user)))
                      .collect(Collectors.toSet());
    }
}
