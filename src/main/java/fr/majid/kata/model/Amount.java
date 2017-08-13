package fr.majid.kata.model;

public class Amount {
	private long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public Amount(long value) {
		this.value = value;
	}

	private long validate(long value) {
		if (value > 0) {
			return value;
		}
		throw new IllegalArgumentException("Amount should be positif");
	}
	public Amount() {
    }
}
