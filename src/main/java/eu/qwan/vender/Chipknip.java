package eu.qwan.vender;

public class Chipknip {
	public int credits;

	public Chipknip(int initialValue) {
		credits = initialValue;
	}

	public boolean hasValue(int p) {
		return credits >= p;
	}

	public void reduce(int p) {
		credits -= p;
	}
}
