package org.freeshell.rfcornel.util;

public class Pair<T1, T2> {
	public final  T1 first;
	public final  T2 second;
	
	private Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof  Pair)) {
			return false;
		}

		Pair typedObj = (Pair) obj;
		return first.equals(typedObj.first) && second.equals(typedObj.second);
	}

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }

    @Override
	public String toString() {
		return "[" + this.first.toString() + ", " + this.second.toString() + "]";
	}

	public static<T3, T4> Pair<T3, T4> of(T3 value1, T4 value2) {
		return new Pair<>(value1, value2);
	}
}
