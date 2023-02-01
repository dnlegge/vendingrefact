package eu.qwan.vender;

import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
	private final Map<Choice, CanContainer> cans = new HashMap<Choice, CanContainer>();
	private int paymentMethod;
	private Chipknip chipknip;
	private int coins = -1;
	private int price;

	public void addCoins(int coinsToBeAdded) {
		paymentMethod = 1;
		if (coins != -1) {
			coins += coinsToBeAdded;
		} else {
			coins = coinsToBeAdded;
		}
	}

	public void insertChip(Chipknip chipknip) {
		// TODO
		// can't pay with chip in brittain
		paymentMethod = 2;
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
				case 1: // paying with coins
					if (coins != -1 && cans.get(choice).price <= coins) {
						res = cans.get(choice).getType();
						coins -= cans.get(choice).price;
					}
					break;
				case 2: // paying with chipknip -
					// TODO: if this machine is in belgium this must be an error
					// {
					if (chipknip.hasValue(cans.get(choice).price)) {
						chipknip.reduce(cans.get(choice).price);
						res = cans.get(choice).getType();
					}
					break;
				default:
					// TODO: Is this a valid situation?:
					// larry forgot the } else { clause
					// i added it, but i am acutally not sure as to wether this
					// is a problem
					// unknown payment
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

	public int getChange() {
		int toReturn = 0;
		if (coins > 0) {
			toReturn = coins;
			coins = 0;
		}
		return toReturn;
	}

	public void configure(Choice choice, Can c, int amount) {
		configure(choice, c, amount, 0);
	}

	public void configure(Choice choice, Can c, int amount, int price) {
		this.price = price;
		if (cans.containsKey(choice)) {
			cans.get(choice).setAmount(cans.get(choice).getAmount() + amount);
			return;
		}
		CanContainer can = new CanContainer();
		can.setType(c);
		can.setAmount(amount);
		can.setPrice(price);
		cans.put(choice, can);
	}
}
