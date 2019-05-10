package eu.qwan.vender;

public class CanContainer {
	private Can type;

    public CanContainer(Can type, int price, int amount) {
        this.type = type;
        this.price = price;
        this.amount = amount;
    }

    public Can getType() {
		return type;
	}

	public void setType(Can type) {
		this.type = type;
	}

	int price;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	private int amount;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
