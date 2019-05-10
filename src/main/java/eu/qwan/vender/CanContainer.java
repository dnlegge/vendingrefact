package eu.qwan.vender;

public class CanContainer {
	private Can type;
    private int price;
    private int amount;

    public CanContainer(Can type, int price, int amount) {
        this.type = type;
        this.price = price;
        this.amount = amount;
    }

    public Can getType() {
		return type;
	}

	public int getPrice() {
		return price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
