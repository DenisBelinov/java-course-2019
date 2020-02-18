package bg.sofia.uni.fmi.mjt.splitwise.backend.service;

import bg.sofia.uni.fmi.mjt.splitwise.backend.data.Debt;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class DebtServiceTest {
    private static final String FROM_USER = "fromUser";
    private static final String TO_USER = "toUser";

    private DebtService debtService;

    @Before
    public void setUp() {
        debtService = new DebtService();
    }

    @Test
    public void testAddNewDebt() {
        double amount = 10;

        debtService.addDebt(FROM_USER, TO_USER, amount);

        Debt debt = debtService.getDebtForUsers(FROM_USER, TO_USER);

        assertEquals(FROM_USER, debt.getFromUsername());
        assertEquals(TO_USER, debt.getToUsername());
        assertEquals(amount, debt.getAmount(), 0.001);
    }

    @Test
    public void testGetDebtForUsersReversedOrder() {
        double amount = 10;

        debtService.addDebt(FROM_USER, TO_USER, amount);

        Debt debt = debtService.getDebtForUsers(TO_USER, FROM_USER);

        assertEquals(FROM_USER, debt.getFromUsername());
        assertEquals(TO_USER, debt.getToUsername());
        assertEquals(amount, debt.getAmount(), 0.001);
    }

    @Test
    public void testGetDebtForUser() {
        double amount = 10;

        debtService.addDebt(FROM_USER, TO_USER, amount);
        debtService.addDebt(FROM_USER, TO_USER + "_1", amount);

        Set<Debt> debts = debtService.getDebtsForUser(FROM_USER);
        assertEquals(2, debts.size());
    }

    @Test
    public void testDebtUpdatedThroughAddDebt() {
        double amount = 10;

        debtService.addDebt(FROM_USER, TO_USER, amount);
        debtService.addDebt(FROM_USER, TO_USER, amount);
        Debt debt = debtService.getDebtForUsers(FROM_USER, TO_USER);

        assertEquals(2 * amount, debt.getAmount(), 0.001);

    }

    @Test
    public void testDebtUpdatedThroughAddPayment() {
        double initialDebtAmount = 10;
        double paybackAmount = 5;

        debtService.addDebt(FROM_USER, TO_USER, initialDebtAmount);
        debtService.addPayment(FROM_USER, TO_USER, paybackAmount);
        Debt debt = debtService.getDebtForUsers(FROM_USER, TO_USER);

        assertEquals(initialDebtAmount - paybackAmount, debt.getAmount(), 0.001);
    }
}