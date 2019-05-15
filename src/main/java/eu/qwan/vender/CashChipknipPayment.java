package eu.qwan.vender;

public class CashChipknipPayment implements Payment {
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    private Chipknip chipknip = null;
    private int cashValueInserted = 0;

    @Override
    public void insertCash(int amountInserted) {
        paymentMethod = PaymentMethod.CASH;
        cashValueInserted += amountInserted;
        chipknip = null;
    }

    @Override
    public void insertChip(Chipknip chipknip) {
        paymentMethod = PaymentMethod.CHIPKNIP;
        this.chipknip = chipknip;
    }

    @Override
    public boolean hasSufficientBalance(int value) {
        if (paymentMethod == PaymentMethod.CHIPKNIP) {
            return chipknip.HasValue(value);
        }
        return value <= cashValueInserted;
    }

    @Override
    public void reduceBalance(int value) {
        if (paymentMethod == PaymentMethod.CHIPKNIP) {
            chipknip.Reduce(value);
            return;
        }
        cashValueInserted -= value;
    }

    @Override
    public int getChange() {
        if (paymentMethod == PaymentMethod.CHIPKNIP) {
            paymentMethod = PaymentMethod.CASH;
            chipknip = null;
            return 0;
        }
        int changeToReturn = cashValueInserted;
        cashValueInserted = 0;
        return changeToReturn;
    }

}
