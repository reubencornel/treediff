package org.freeshell.rfcornel.lcs;

public enum LCSDirection {
	WEST((byte)0),
	NORTHWEST((byte)1),
	NORTH((byte)2);

	public final byte value;

	LCSDirection(byte value) {
		this.value = value;
	}

	public static LCSDirection valueOf(byte value) {
		switch (value) {
			case 0: return WEST;
			case 1: return NORTHWEST;
			case 2: return NORTH;
			default: throw new IllegalArgumentException("Invalid value: " + value);
		}
	}
}
