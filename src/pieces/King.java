package pieces;

import chess.Board;
import math.OrderedPair;

public class King extends Piece {
	
	private static final int VALUE = 0;

	public King(boolean white, OrderedPair pos) {
		super(white, pos, "King");
	}
	
	public King(King k) {
		super(k);
	}
	
	public void updatePossibleMoves() {
		updatePossibleMoves(b);
	}
	
	public void updatePossibleMoves(Board b) {
		resetPossibleMoves();
		OrderedPair test;
		for (int x = -1; x <= 1; x++) {
			next: for (int y = -1; y <= 1; y++) {
				if (x == 0 && y == 0) continue next;
				test = pos.plus(new OrderedPair(x, y));
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
		}
		if (!hasMoved()) {
			if (b.board[pos.x()][0] != null) {
				if (!b.board[pos.x()][0].hasMoved() && !hasMoved()) {
					boolean c = true;
					for (int y = 1; y < 4; y++) {
						if (c) c = b.board[pos.x()][y] == null;
					}
					possibleMoves[pos.x()][2] = c;
				}
			}
			if (b.board[pos.x()][7] != null) {
				if (!b.board[pos.x()][7].hasMoved() && !hasMoved()) {
					boolean c = true;
					for (int y = 5; y < 7; y++) {
						if (c) c = b.board[pos.x()][y] == null;
					}
					possibleMoves[pos.x()][6] = c;
				}
			}
		}
		super.shaveCheck();
	}
	
	@Override
	public void move(OrderedPair pos) {
		if (pos.y() - super.pos.y() == 2) {
			b.physicalMove(new OrderedPair(pos.x(), 7), new OrderedPair(pos.x(), pos.y() - 1));
		}
		if (pos.y() - super.pos.y() == -2) {
			b.physicalMove(new OrderedPair(pos.x(), 0), new OrderedPair(pos.x(), pos.y() + 1));
		}
		super.move(pos);
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public int getValue() {
		return VALUE;
	}

	@Override
	public boolean canMove(OrderedPair o) {
		if (!hasMoved()) {
			if (isWhite()) {
				if (b.blackMoves[pos.x()][pos.y()]) {
					possibleMoves[pos.x()][2] = false;
					possibleMoves[pos.x()][6] = false;
				}
			}
			else {
				if (b.whiteMoves[pos.x()][pos.y()]) {
					possibleMoves[pos.x()][2] = false;
					possibleMoves[pos.x()][6] = false;
				}
			}
		}
		return super.canMove(o);
	}
}
