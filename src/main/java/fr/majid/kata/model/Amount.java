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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amount other = (Amount) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
