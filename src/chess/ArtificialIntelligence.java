package chess;

import java.util.ArrayList;

import math.OrderedPair;
import pieces.Piece;

public class ArtificialIntelligence {
	
	private Board b;
	private ArrayList<PieceMoves> pieces;

	public ArtificialIntelligence(Board b) {
		this.b = b;
		pieces = new ArrayList<PieceMoves>();
	}
	
	public void move() {
		setPieces();
		int[] order = createRandomOrder(pieces.size());
		for (OrderedPair x : pieces.get(order[0]).getSafeMoves())
			System.out.println(pieces.get(order[0]).pos + "   " + x);
		next: for (int x : order) {
			if (pieces.get(order[x]).getSafeMoves().length == 0) continue next;
			int pick = (int) (Math.random() * pieces.get(order[x]).getSafeMoves().length);
			b.move(pieces.get(order[x]).pos, pieces.get(order[x]).getSafeMoves()[pick]);
			return;
		}
		System.out.println("No safe moves");
		next: for (int x : order) {
			if (pieces.get(order[x]).getMoves().length == 0) continue next;
			int pick = (int) (Math.random() * pieces.get(order[x]).getMoves().length);
			b.move(pieces.get(order[x]).pos, pieces.get(order[x]).getMoves()[pick]);
			return;
		}
	}
	
	private void setPieces() {
		pieces.clear();
		for (Piece[] q : b.board) {
			for (Piece p : q) {
				if (p != null) {
					if (p.isWhite() == b.whiteTurn())
						pieces.add(new PieceMoves(p.getPos()));
				}
			}
		}
	}
	
	public static int[] createRandomOrder(int size) {
		int[] ret = new int[size];
		next: for (int x = 0; x < size; x++) {
			int z = (int) (Math.random() * size);
			for (int y = 0; y < x; y++) {
				if (ret[y] == z) {
					x--;
					continue next;
				}
			}
			ret[x] = z;
		}
		return ret;
	}
	
	@SuppressWarnings("unused")
	private boolean[][] aNotB(boolean[][] a, boolean[][] b) {
		boolean[][] ret = new boolean[8][8];
		for (int x = 0; x < 64; x++) {
			ret[x % 8][x / 8] = a[x % 8][x / 8] && !b[x % 8][x / 8];
		}
		return ret;
	}
	
	private class PieceMoves {
		private OrderedPair pos;
		private OrderedPair[] allMoves;
		private OrderedPair[] safeMoves;
		private Piece piece;
		
		public PieceMoves(OrderedPair o) {
			this.allMoves = new OrderedPair[0];
			this.safeMoves = new OrderedPair[0];
			this.pos = new OrderedPair(o);
			this.piece = b.get(pos);
			setMoves();
		}
		
		public void setMoves() {
			this.setAllMoves();
			this.setSafeMoves();
		}
		
		public OrderedPair[] getMoves() {
			return allMoves;
		}
		
		public OrderedPair[] getSafeMoves() {
			return safeMoves;
		}
		
		private void setAllMoves() {
			for (int x = 0; x < 64; x++) {
				if (piece.getPossibleMoves()[x % 8][x / 8])
					allMoves = add(allMoves, new OrderedPair(x));
			}
		}
		
		private void setSafeMoves() {
			for (OrderedPair x : allMoves) {
				Board n = new Board(b);
				n.move(pos, x);
				if (piece.isWhite()) {
					if (!b.blackMoves[x.x()][x.y()])
						safeMoves = add(safeMoves, x);
				}
				else {
					if (!b.whiteMoves[x.x()][x.y()])
						safeMoves = add(safeMoves, x);
				}
			}
		}
		
		private OrderedPair[] add(OrderedPair[] o, OrderedPair p) {
			if (o == null) return new OrderedPair[]{p};
			OrderedPair[] ret = new OrderedPair[o.length + 1];
			for (int x = 0; x < o.length; x++) {
				ret[x] = o[x];
			}
			ret[o.length] = p;
			return ret;
		}
	}
}
