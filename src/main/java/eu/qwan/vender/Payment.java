package eu.qwan.vender;

public interface Payment {
    void insertCash(int amountInserted);

    void insertChip(Chipknip chipknip);

    boolean hasSufficientBalance(int value);

    void reduceBalance(int value);

    int getChange();
}
