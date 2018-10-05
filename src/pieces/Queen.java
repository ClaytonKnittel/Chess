package pieces;

import chess.Board;
import math.OrderedPair;

public class Queen extends Piece {
	
	private static final int VALUE = 9;

	public Queen(boolean white, OrderedPair pos) {
		super(white, pos, "Queen");
	}
	
	public Queen(Queen q) {
		super(q);
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
