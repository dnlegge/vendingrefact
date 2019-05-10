package eu.qwan.vender;

import java.util.HashMap;
import java.util.Map;

public class ChipknipVendingMachine implements VendingMachine {
    private final Map<Choice, CanContainer> cans = new HashMap<>();
    private PaymentMethod paymentMethod = PaymentMethod.NONE;
    private Chipknip chipknip;
    private int cashValueInserted = 0;

    @Override
    public void insertCash(int amountInserted) {
        paymentMethod = PaymentMethod.CASH;
        cashValueInserted += amountInserted;
    }

    @Override
    public void insertChip(Chipknip chipknip) {
        // TODO
        // can't pay with chip in brittain
        paymentMethod = PaymentMethod.CHIPKNIP;
        this.chipknip = chipknip;
    }

    // delivers the can if all ok {
    @Override
    public Can deliver(Choice choice) {
        //
        // step 1: check if choice exists {
        //
        if (!cans.containsKey(choice)) {
            return Can.none;
        }

        CanContainer canContainer = cans.get(choice);

        //
        // step2 : check price
        //
        Can res = handlePayment(canContainer);

        if (res == Can.none) {
            return res;
        }

        //
        // step 3: check stock
        //
        if (canContainer.getAmount() <= 0) {
            return Can.none;
        }

        canContainer.setAmount(canContainer.getAmount() - 1);
        return res;
    }

    private Can handlePayment(CanContainer canContainer) {
        if (canContainer.getPrice() == 0) {
            return canContainer.getType();
            // or price matches
        }

        switch (paymentMethod) {
            case CASH: // paying with coins
                if (canContainer.getPrice() <= cashValueInserted) {
                    cashValueInserted -= canContainer.getPrice();
                    return canContainer.getType();
                }
                break;
            case CHIPKNIP: // paying with chipknip -
                // TODO: if this machine is in belgium this must be an error
                if (chipknip.HasValue(canContainer.getPrice())) {
                    chipknip.Reduce(canContainer.getPrice());
                    return canContainer.getType();
                }
                break;
            default:
                break;
        }

        return Can.none;
    }

    @Override
    public int getChange() {
        int toReturn = cashValueInserted;
        cashValueInserted = 0;
        return toReturn;
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
