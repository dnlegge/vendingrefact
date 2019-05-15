package eu.qwan.vender;

import java.util.HashMap;
import java.util.Map;

public class ChipknipVendingMachine implements VendingMachine {
    private final Map<Choice, CanContainer> canContainerMap = new HashMap<>();
    private Payment payment = new CashChipknipPayment();

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
        if (!canContainerMap.containsKey(choice)) {
            return Can.none;
        }

        CanContainer canContainer = canContainerMap.get(choice);

        //free vend
        int price = canContainer.getPrice();
        if (price == 0) {
            return getCanOrNone(canContainer);
        }

        if (payment.hasSufficientBalance(price)) {
            Can canOrNone = getCanOrNone(canContainer);
            if (canOrNone != Can.none) {
                payment.reduceBalance(price);
            }
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

    public void configure(Choice choice, Can typeOfCan, int numberOfCans) {
        configure(choice, typeOfCan, numberOfCans, 0);
    }

    public void configure(Choice choice, Can typeOfCan, int numberOfCans, int price) {
        if (canContainerMap.containsKey(choice)) {
            canContainerMap.get(choice).setAmount(canContainerMap.get(choice).getAmount() + numberOfCans);
            return;
        }
        CanContainer canContainer = new CanContainer(typeOfCan, price, numberOfCans);
        canContainerMap.put(choice, canContainer);
    }
}
