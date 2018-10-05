package pieces;

import chess.Board;
import math.OrderedPair;

public class Pawn extends Piece {
	
	private static final int VALUE = 1;

	public Pawn(boolean white, OrderedPair pos) {
		super(white, pos, "Pawn");
	}
	
	public Pawn(Pawn p) {
		super(p);
	}
	
	public void updatePossibleMoves() {
		updatePossibleMoves(b);
	}
	
	public void updatePossibleMoves(Board b) {
		resetPossibleMoves();
		OrderedPair test;
		int x;
		if (isWhite()) x = 1;
		else x = -1;
		for (int y = -1; y <= 1; y += 2) {
			test = pos.plus(new OrderedPair(x, y));
			if (test.insideBoard()) {
				if (b.get(test) == null) {}
				else if (b.get(test).isWhite() != isWhite()) {
					possibleMoves[test.x()][test.y()] = true;
				}
			}
		}
		test = pos.plus(new OrderedPair(x, 0));
		if (b.get(test) == null && test.insideBoard()) {
			possibleMoves[test.x()][test.y()] = true;
			if (!hasMoved()) {
				test = pos.plus(new OrderedPair(2 * x, 0));
				if (b.get(test) == null)
					possibleMoves[test.x()][test.y()] = true;
			}
		}
		super.shaveCheck();
	}
	
	@Override
	public void move(OrderedPair pos) {
		super.move(pos);
		if (!b.isTemporary() && ((pos.x == 7 && isWhite()) || (pos.x == 0 && !isWhite())))
			b.queenify(pos);
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
