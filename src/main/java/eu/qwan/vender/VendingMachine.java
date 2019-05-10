package eu.qwan.vender;

import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private final Map<Choice, CanContainer> cans = new HashMap<>();
    private PaymentMethod paymentMethod = PaymentMethod.NONE;
    private Chipknip chipknip;
    private int c = -1;
    private int price;

    public void set_value(int v) {
        paymentMethod = PaymentMethod.CASH;
        if (c != -1) {
            c += v;
        } else {
            c = v;
        }
    }

    public void insert_chip(Chipknip chipknip) {
        // TODO
        // can't pay with chip in brittain
        paymentMethod = PaymentMethod.CHIPKNIP;
        this.chipknip = chipknip;
    }

    // delivers the can if all ok {
    public Can deliver(Choice choice) {
        Can res = Can.none;
        //
        // step 1: check if choice exists {
        //
        if (cans.containsKey(choice)) {
            //
            // step2 : check price
            //
            if (cans.get(choice).price == 0) {
                res = cans.get(choice).getType();
                // or price matches
            } else {

                switch (paymentMethod) {
                    case CASH: // paying with coins
                        if (c != -1 && cans.get(choice).price <= c) {
                            res = cans.get(choice).getType();
                            c -= cans.get(choice).price;
                        }
                        break;
                    case CHIPKNIP: // paying with chipknip -
                        // TODO: if this machine is in belgium this must be an error
                        // {
                        if (chipknip.HasValue(cans.get(choice).price)) {
                            chipknip.Reduce(cans.get(choice).price);
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
        } else {
            res = Can.none;
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

    public int get_change() {
        int to_return = 0;
        if (c > 0) {
            to_return = c;
            c = 0;
        }
        return to_return;
    }

    public void configure(Choice choice, Can c, int n) {
        configure(choice, c, n, 0);
    }

    public void configure(Choice choice, Can c, int n, int price) {
        this.price = price;
        if (cans.containsKey(choice)) {
            cans.get(choice).setAmount(cans.get(choice).getAmount() + n);
            return;
        }
        CanContainer can = new CanContainer();
        can.setType(c);
        can.setAmount(n);
        can.setPrice(price);
        cans.put(choice, can);
    }
}
