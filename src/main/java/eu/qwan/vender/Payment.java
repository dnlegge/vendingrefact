package eu.qwan.vender;

public class Payment {
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    private Chipknip chipknip = null;
    private int cashValueInserted = 0;

    public void insertCash(int amountInserted) {
        paymentMethod = PaymentMethod.CASH;
        cashValueInserted += amountInserted;
        chipknip = null;
    }

    public void insertChip(Chipknip chipknip) {
        paymentMethod = PaymentMethod.CHIPKNIP;
        this.chipknip = chipknip;
    }

    public boolean hasSufficientBalance(int value) {
        if (paymentMethod == PaymentMethod.CHIPKNIP) {
            return chipknip.HasValue(value);
        }
        return value <= cashValueInserted;
    }

    public void reduceBalance(int value) {
        if (paymentMethod == PaymentMethod.CHIPKNIP) {
            chipknip.Reduce(value);
            return;
        }
        cashValueInserted -= value;
    }

    public int getChange() {
        if (paymentMethod == PaymentMethod.CHIPKNIP) {
            paymentMethod = PaymentMethod.CASH;
            return 0;
        }
        int changeToReturn = cashValueInserted;
        cashValueInserted = 0;
        return changeToReturn;
    }

}
