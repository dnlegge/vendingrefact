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
        Can res = Can.none;
        //
        // step 1: check if choice exists {
        //
        if (!cans.containsKey(choice)) {
            return Can.none;
        }

        //
        // step2 : check price
        //
        if (cans.get(choice).getPrice() == 0) {
            res = cans.get(choice).getType();
            // or price matches
        } else {

            switch (paymentMethod) {
                case CASH: // paying with coins
                    if (cashValueInserted != -1 && cans.get(choice).getPrice() <= cashValueInserted) {
                        res = cans.get(choice).getType();
                        cashValueInserted -= cans.get(choice).getPrice();
                    }
                    break;
                case CHIPKNIP: // paying with chipknip -
                    // TODO: if this machine is in belgium this must be an error
                    // {
                    if (chipknip.HasValue(cans.get(choice).getPrice())) {
                        chipknip.Reduce(cans.get(choice).getPrice());
                        res = cans.get(choice).getType();
                    }
                    break;
                default:
                    // TODO: Is this a valid situation?:
                    // larry forgot the } else { clause
                    // i added it, but i am acutally not sure as to wether this
                    // is a problem
                    // unknown payment
//                        res =  Can.none;
//                        return Can.none;
                    break;
                // i think(i) nobody inserted anything
            }
        }

        //
        // step 3: check stock
        //
        if (res != Can.none) {
            if (cans.get(choice).getAmount() <= 0) {
                res = Can.none;
            } else {
                cans.get(choice).setAmount(cans.get(choice).getAmount() - 1);
            }
        }

        // if can is set then return {
        // otherwise we need to return the none
        if (res == Can.none) {
            return Can.none;
        }

        return res;
    }

    @Override
    public int getChange() {
        int to_return = 0;
        if (cashValueInserted > 0) {
            to_return = cashValueInserted;
            cashValueInserted = 0;
        }
        return to_return;
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
