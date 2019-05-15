package eu.qwan.vender;

import java.util.HashMap;
import java.util.Map;

public class ChipknipVendingMachine implements VendingMachine {
    private final Map<Choice, CanContainer> cans = new HashMap<>();
    private Payment payment = new Payment();

    @Override
    public void insertCash(int amountInserted) {
        payment.insertCash(amountInserted);
    }

    @Override
    public void insertChip(Chipknip chipknip) {
        payment.insertChip(chipknip);
    }

    @Override
    public Can deliver(Choice choice) {

        // step 1: check if choice exists {
        if (!cans.containsKey(choice)) {
            return Can.none;
        }

        CanContainer canContainer = cans.get(choice);

        //free vend
        int price = canContainer.getPrice();
        if (price == 0) {
            return getCanOrNone(canContainer);
        }

        if (payment.hasSufficientBalance(price)) {
            Can canOrNone = getCanOrNone(canContainer);
            payment.reduceBalance(price);
            return canOrNone;
        }

        return Can.none;
    }

    private Can getCanOrNone(CanContainer canContainer) {
        if (stockIsAvailable(canContainer)) {
            return Can.none;
        }

        canContainer.setAmount(canContainer.getAmount() - 1);
        return canContainer.getType();
    }

    private boolean stockIsAvailable(CanContainer canContainer) {
        return canContainer.getAmount() <= 0;
    }

    @Override
    public int getChange() {
        return payment.getChange();
    }

    public void configure(Choice choice, Can c, int n) {
        configure(choice, c, n, 0);
    }

    public void configure(Choice choice, Can can, int n, int price) {
        if (cans.containsKey(choice)) {
            cans.get(choice).setAmount(cans.get(choice).getAmount() + n);
            return;
        }
        CanContainer canContainer = new CanContainer(can, price, n);
        cans.put(choice, canContainer);
    }
}
