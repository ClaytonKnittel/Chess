package pieces;

import chess.Board;
import math.OrderedPair;

public class Bishop extends Piece {
	
	private static final int VALUE = 3;

	public Bishop(boolean white, OrderedPair pos) {
		super(white, pos, "Bishop");
	}
	
	public Bishop(Bishop b) {
		super(b);
	}
	
	public void updatePossibleMoves() {
		updatePossibleMoves(b);
	}
	
	public void updatePossibleMoves(Board b) {
		resetPossibleMoves();
		OrderedPair test;
		for (int dx = -1; dx <= 1; dx += 2) {
			next: for (int dy = -1; dy <= 1; dy += 2) {
				for (int c = 1; c < 8; c++) {
					test = pos.plus(new OrderedPair(dx * c, dy * c));
					if (test.insideBoard()) {
						if (b.get(test) == null) {
							possibleMoves[test.x()][test.y()] = true;
						}
						else if (b.get(test).isWhite() != isWhite()) {
							possibleMoves[test.x()][test.y()] = true;
							continue next;
						}
						else continue next;
					}
					else continue next;
				}
			}
		}
		super.shaveCheck();
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public int getValue() {
		return VALUE;
	}
}
