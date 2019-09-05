package org.freeshell.rfcornel.lcs;

/**
 * This class represents the result of a single LCS cell.
 * 
 * @author rcornel
 */
public class LCSCellResult {
	public final LCSDirection direction;
	public final int cost;
	
	public LCSCellResult(LCSDirection direction, int cost) {
		this.direction = direction;
		this.cost = cost;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		
		if (!(other instanceof LCSCellResult)) {
			return false;
		}
		
		LCSCellResult otherResult = (LCSCellResult)other;
	
		return this.direction == otherResult.direction && this.cost == otherResult.cost;
	}

	@Override
	public String toString() {
		return "<" + this.direction.name() + " " + this.cost + ">";
	}
}
