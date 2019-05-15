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

        switch (payment.paymentMethod) {
            case CASH:
                if (canContainer.getPrice() <= payment.getCashBalance()) {
                    Can res = getCanOrNone(canContainer);
                    payment.insertCash(-canContainer.getPrice());
                    return res;
                }
                break;
            case CHIPKNIP:
                // TODO: if this machine is in belgium this must be an error
                if (payment.hasValue(canContainer.getPrice())) {
                    Can res = getCanOrNone(canContainer);
                    payment.getChipknip().Reduce(canContainer.getPrice());
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
        if (payment.paymentMethod == PaymentMethod.CASH) {
            int toReturn = payment.getCashBalance();
            payment.insertCash(-toReturn);
            return toReturn;
        }
        return 0;
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
