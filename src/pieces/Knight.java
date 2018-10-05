package pieces;

import chess.Board;
import math.OrderedPair;

public class Knight extends Piece {
	
	private static final int VALUE = 3;
	private static final OrderedPair[] MOVES = new OrderedPair[]{new OrderedPair(-1, 2), new OrderedPair(-1, -2), new OrderedPair(1, 2), new OrderedPair(1, -2), new OrderedPair(-2, 1), new OrderedPair(-2, -1), new OrderedPair(2, 1), new OrderedPair(2, -1)};
	
	public Knight(boolean white, OrderedPair pos) {
		super(white, pos, "Knight");
	}
	
	public Knight(Knight k) {
		super(k);
	}
	
	public void updatePossibleMoves() {
		updatePossibleMoves(b);
	}
	
	public void updatePossibleMoves(Board b) {
		resetPossibleMoves();
		OrderedPair test;
		next: for (int x = 0; x < MOVES.length; x++) {
			test = pos.plus(MOVES[x]);
			if (test.insideBoard()) {
				if (b.get(test) == null)
					possibleMoves[test.x()][test.y()] = true;
				else if (b.get(test).isWhite() != isWhite()) {
					possibleMoves[test.x()][test.y()] = true;
					continue next;
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
