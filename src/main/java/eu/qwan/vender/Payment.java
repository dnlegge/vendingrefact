package eu.qwan.vender;

public class Payment {
    PaymentMethod paymentMethod = PaymentMethod.CASH;
    private Chipknip chipknip;
    private int cashValueInserted = 0;

    public void insertCash(int amountInserted) {
        paymentMethod = PaymentMethod.CASH;
        cashValueInserted += amountInserted;
    }

    public void insertChip(Chipknip chipknip) {
        paymentMethod = PaymentMethod.CHIPKNIP;
        this.chipknip = chipknip;
    }

    public int getCashBalance() {
        return cashValueInserted;
    }

    public Chipknip getChipknip() {
        return chipknip;
    }

    public boolean hasValue(int value) {
        return chipknip.HasValue(value);
    }

}
