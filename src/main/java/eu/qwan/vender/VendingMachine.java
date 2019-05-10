package eu.qwan.vender;

public interface VendingMachine {
    void insertCash(int v);

    void insertChip(Chipknip chipknip);

    // delivers the can if all ok {
    Can deliver(Choice choice);

    int getChange();
}
