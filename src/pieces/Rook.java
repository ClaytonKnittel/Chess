package pieces;

import chess.Board;
import math.OrderedPair;

public class Rook extends Piece {
	
	private static final int VALUE = 5;

	public Rook(boolean white, OrderedPair pos) {
		super(white, pos, "Rook");
	}
	
	public Rook(Rook r) {
		super(r);
	}
	
	public void updatePossibleMoves() {
		updatePossibleMoves(b);
	}
	
	public void updatePossibleMoves(Board b) {
		resetPossibleMoves();
		OrderedPair test;
		next: for (int delta = 0; delta < 4; delta ++) {
			for (int x = 1; x < 8; x++) {
				test = pos.plus(new OrderedPair(x * ((delta - 1) % 2), x * ((delta - 2) % 2)));
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
