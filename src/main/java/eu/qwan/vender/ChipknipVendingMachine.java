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
        // can't pay with chip in Britain
        paymentMethod = PaymentMethod.CHIPKNIP;
        this.chipknip = chipknip;
    }

    // delivers the can if all ok {
    @Override
    public Can deliver(Choice choice) {

        // step 1: check if choice exists {
        if (!cans.containsKey(choice)) {
            return Can.none;
        }

        CanContainer canContainer = cans.get(choice);

        //free vend
        if (canContainer.getPrice() == 0) {
            return getCanOrNone(canContainer);
        }

        switch (paymentMethod) {
            case CASH:
                if (canContainer.getPrice() <= cashValueInserted) {
                    Can res = getCanOrNone(canContainer);
                    cashValueInserted -= canContainer.getPrice();
                    return res;
                }
                break;
            case CHIPKNIP:
                // TODO: if this machine is in belgium this must be an error
                if (chipknip.HasValue(canContainer.getPrice())) {
                    Can res = getCanOrNone(canContainer);
                    chipknip.Reduce(canContainer.getPrice());
                    return res;
                }
                break;
            default:
                break;
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
