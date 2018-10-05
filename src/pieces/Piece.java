package pieces;

import chess.Board;
import director.Director;
import graphics.PieceGraphics;
import math.OrderedPair;
import shapes.Shape;

public abstract class Piece {

	private boolean white;
	private boolean hasMoved = false;
	protected OrderedPair pos;
	protected boolean[][] possibleMoves;
	protected boolean[][] possibleAttacks;
	private PieceGraphics graphics;
	protected Board b;
	
	public Piece(boolean white, OrderedPair pos, String s) {
		this.white = white;
		this.pos = pos;
		possibleMoves = new boolean[8][8];
		possibleAttacks = new boolean[8][8];
		graphics = new PieceGraphics(pos, white, s);
		Director.world.add(graphics);
	}
	
	public Piece(Piece p) {
		this.white = p.isWhite();
		this.pos = p.pos;
		possibleMoves = new boolean[8][8];
		possibleAttacks = new boolean[8][8];
		this.hasMoved = p.hasMoved();
	}
	
	public void addBoard(Board b) {
		this.b = b;
	}
	
	public boolean isWhite() {
		return white;
	}
	
	public OrderedPair getPos() {
		return pos;
	}
	
	protected void resetPossibleMoves() {
		for (int x = 0; x < 64; x++) {
			possibleMoves[x % 8][x / 8] = false;
			possibleAttacks[x % 8][x / 8] = false;
		}
	}
	
	public void move(OrderedPair pos) {
		hasMoved = true;
		this.pos = pos;
		if (!b.isTemporary())
			graphics.move(pos);
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public boolean canMove(int x) {
		return canMove(new OrderedPair(x % 8, x / 8));
	}
	
	public boolean canAttack(int x) {
		return canAttack(new OrderedPair(x % 8, x / 8));
	}
	
	public void die() {
		if (!b.isTemporary()) {
			Director.world.remove(graphics);
			graphics = null;
		}
	}
	
	public Shape getGraphics() {
		return graphics;
	}
	
	public boolean[][] getPossibleMoves() {
		return possibleMoves;
	}
	
	public boolean canMove(OrderedPair o) {
		return possibleMoves[o.x()][o.y()];
	}
	
	protected void shaveCheck() {
		if (b.whiteTurn() != isWhite())
			return;
		for (int x = 0; x < 64; x++) {
			if (possibleMoves[x % 8][x / 8])
				possibleMoves[x % 8][x / 8] = !checkCheck(new OrderedPair(x % 8, x / 8));
		}
	}
	
	private boolean checkCheck(OrderedPair o) {
		if (!b.isTemporary()) {
			Board n = new Board(b);
			n.move(pos, o);
			if (isWhite())
				return n.whiteCheck;
			else
				return n.blackCheck;
		} else return false;
	}

	public boolean canAttack(OrderedPair o) {
		return possibleAttacks[o.x()][o.y()];
	}

	public abstract void updatePossibleMoves();
	public abstract int getValue() ;
}
