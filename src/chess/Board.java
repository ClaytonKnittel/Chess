package chess;

import director.Director;
import graphics.BoardGraphics;
import graphics.Color;
import inputs.MouseListen;
import math.OrderedPair;
import math.Vector;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import shapes.Shape;

public class Board {

	public Piece[][] board;
	public boolean[][] whiteMoves;	//spots white pieces can potentially move to
	public boolean[][] blackMoves;
	private boolean whiteTurn;
	private OrderedPair selectedPiece;
	public boolean whiteCheck;
	public boolean blackCheck;
	private boolean temporary;
	
	private static BoardGraphics graphics;
	public boolean cheats;
	public boolean AI;
	private ArtificialIntelligence ai;
	private boolean aiWhite;
	
	public static void Initiate(int width, int height, boolean cheats, boolean AI, boolean aiWhite) {
		new Board(width, height, cheats, AI, aiWhite);
	}
	
	public Board(int width, int height, boolean cheats, boolean AI, boolean aiWhite) {
		Director.init(width, height, 0, 70, -60, 0, -0.9f);
		
		MouseListen mouse = new MouseListen(this);
		Director.graphics.addMouseListener(mouse);
		
		board = new Piece[8][8];
		whiteMoves = new boolean[8][8];
		blackMoves = new boolean[8][8];
		whiteTurn = true;
		this.cheats = cheats;
		this.AI = AI;
		if (AI) {
			ai = new ArtificialIntelligence(this);
			this.aiWhite = aiWhite;
		}
		temporary = false;
		reset();
	}
	
	public Board(Board b) {				//only for temporary Boards (for checking if a move will put king in check)
		board = new Piece[8][8];
		for (int x = 0; x < 64; x++) {
			Piece p = b.board[x % 8][x / 8];
			if (p instanceof Bishop)
				board[x % 8][x / 8] = new Bishop((Bishop) p);
			else if (p instanceof King)
				board[x % 8][x / 8] = new King((King) p);
			else if (p instanceof Knight)
				board[x % 8][x / 8] = new Knight((Knight) p);
			else if (p instanceof Pawn)
				board[x % 8][x / 8] = new Pawn((Pawn) p);
			else if (p instanceof Queen)
				board[x % 8][x / 8] = new Queen((Queen) p);
			else if (p instanceof Rook)
				board[x % 8][x / 8] = new Rook((Rook) p);
		}
		whiteMoves = new boolean[8][8];
		blackMoves = new boolean[8][8];
		whiteTurn = b.whiteTurn;
		selectedPiece = b.selectedPiece;
		temporary = true;
		addBoard();
		updatePossibleMoves();
	}
	
	public void reset() {
		for (int x = 0; x < 64; x++) {
			board[x/8][x%8] = null;
		}
		for (int x = 0; x < 8; x++)
			board[1][x] = new Pawn(true, new OrderedPair(1, x));
		board[0][0] = new Rook(true, new OrderedPair(0, 0));
		board[0][7] = new Rook(true, new OrderedPair(0, 7));
		board[0][1] = new Knight(true, new OrderedPair(0, 1));
		board[0][6] = new Knight(true, new OrderedPair(0, 6));
		board[0][2] = new Bishop(true, new OrderedPair(0, 2));
		board[0][5] = new Bishop(true, new OrderedPair(0, 5));
		board[0][3] = new Queen(true, new OrderedPair(0, 3));
		board[0][4] = new King(true, new OrderedPair(0, 4));
		for (int x = 0; x < 8; x++)
			board[6][x] = new Pawn(false, new OrderedPair(6, x));
		board[7][0] = new Rook(false, new OrderedPair(7, 0));
		board[7][7] = new Rook(false, new OrderedPair(7, 7));
		board[7][1] = new Knight(false, new OrderedPair(7, 1));
		board[7][6] = new Knight(false, new OrderedPair(7, 6));
		board[7][2] = new Bishop(false, new OrderedPair(7, 2));
		board[7][5] = new Bishop(false, new OrderedPair(7, 5));
		board[7][3] = new Queen(false, new OrderedPair(7, 3));
		board[7][4] = new King(false, new OrderedPair(7, 4));
		addBoard();
		updatePossibleMoves();
		graphics = new BoardGraphics(Color.WOODEN_BROWN, this);
	}
	
	private void addBoard() {
		for (int x = 0; x < 64; x++) {
			if (board[x % 8][x / 8] != null)
				board[x % 8][x / 8].addBoard(this);
		}
	}
	
	public boolean isTemporary() {
		return temporary;
	}
	
	public boolean whiteTurn() {
		return whiteTurn;
	}
	
	public boolean pieceSelected() {
		return selectedPiece != null;
	}
	
	public boolean cheats() {
		return cheats;
	}
	
	public boolean canMove(int x) {
		boolean r = can(x);
		if (!r) clear();
		return r;
	}
	
	private boolean can(int x) {
		if (x == -1)
			return false;
		if (whiteTurn) {
			if (selectedPiece != null)
				return board[selectedPiece.x()][selectedPiece.y()].canMove(x);
			if (board[x % 8][x / 8] == null)
				return false;
			return board[x % 8][x / 8].isWhite();
		} else {
			if (selectedPiece != null)
				return board[selectedPiece.x()][selectedPiece.y()].canMove(x);
			if (board[x % 8][x / 8] == null)
				return false;
			return !board[x % 8][x / 8].isWhite();
		}
	}
	
	public boolean isEmpty(boolean[][] a) {
		for (boolean[] b : a) {
			for (boolean c : b) {
				if (c) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean canMove(OrderedPair i, OrderedPair f) {
		return board[i.x()][i.y()].canMove(f);
	}
	
	public void move (OrderedPair i, OrderedPair f) {
		if (board[f.x()][f.y()] != null)
			board[f.x()][f.y()].die();
		physicalMove(i, f);
		clear();
		whiteTurn = !whiteTurn;
		updatePossibleMoves();
		if (AI && aiWhite == whiteTurn && !temporary)
			ai.move();
	}
	
	public void physicalMove(OrderedPair i, OrderedPair f) {
		board[f.x()][f.y()] = board[i.x()][i.y()];
		board[i.x()][i.y()] = null;
		board[f.x()][f.y()].move(f);
	}
	
	public void queenify(OrderedPair pos) {
		board[pos.x()][pos.y()].die();
		board[pos.x()][pos.y()] = new Queen(board[pos.x()][pos.y()].isWhite(), pos);
		board[pos.x()][pos.y()].addBoard(this);
	}
	
	public void click(Vector v) {
		graphics.updateClicker(v);
	}
	
	public void select(OrderedPair o) {
		if (selectedPiece == null)
			selectedPiece = o;
		else
			move(selectedPiece, o);
	}
	
	public void clear() {
		selectedPiece = null;
	}
	
	public void updatePossibleMoves() {
		whiteCheck = false;
		blackCheck = false;
		for (int x = 0; x < 64; x++) {
			whiteMoves[x % 8][x / 8] = false;
			blackMoves[x % 8][x / 8] = false;
		}
		for (int x = 0; x < 64; x++) {
			if (board[x % 8][x / 8] != null) {
				board[x % 8][x / 8].updatePossibleMoves();
				for (int y = 0; y < 64; y++) {
					if (board[x % 8][x / 8].isWhite()) {
						whiteMoves[y % 8][y / 8] = board[x % 8][x / 8].canMove(y) || whiteMoves[y % 8][y / 8];
					}
					else {
						blackMoves[y % 8][y / 8] = board[x % 8][x / 8].canMove(y) || blackMoves[y % 8][y / 8];
					}
				}
			}
		}
		for (int x = 0; x < 64; x++) {
			if (board[x % 8][x / 8] instanceof King) {
				if (blackMoves[x % 8][x / 8] && board[x % 8][x / 8].isWhite())
					whiteCheck = true;
				if (whiteMoves[x % 8][x / 8] && !board[x % 8][x / 8].isWhite())
					blackCheck = true;
			}
		}
		if (!temporary)
			checkCheckmate();
	}
	
	public void checkCheckmate() {
		if (whiteTurn) {
			for (Piece[] p : board) {
				for (Piece a : p) {
					for (int x = 0; x < 64; x++) {
						if (a != null) {
							if (a.isWhite() && a.canMove(x))
								return;
						}
					}
				}
			}
			if (whiteCheck) System.out.println("Checkmate, black wins");
			else System.out.println("Stalemate");
		} else {
			for (Piece[] p : board) {
				for (Piece a : p) {
					for (int x = 0; x < 64; x++) {
						if (a != null) {
							if (!a.isWhite() && a.canMove(x))
								return;
						}
					}
				}
			}
			if (blackCheck) System.out.println("Checkmate, white wins");
			else System.out.println("Stalemate");
		}
	}
	
	public Piece getSelectedPiece() {
		if (selectedPiece == null) System.out.println("FEFEE");
		return board[selectedPiece.x()][selectedPiece.y()];
	}
	
	public Shape[] getPieceGraphics() {
		Shape[] ret = new Shape[0];
		for (int x = 0; x < 64; x++) {
			if (board[x / 8][x % 8] != null) {
				if (board[x / 8][x % 8].getGraphics() != null)
					ret = add(ret, board[x / 8][x % 8].getGraphics());
			}
		}
		return ret;
	}
	
	private static Shape[] add(Shape[] a, Shape b) {
		Shape[] ret = new Shape[a.length + 1];
		for (int x = 0; x < a.length; x++)
			ret[x] = a[x];
		ret[a.length] = b;
		return ret;
	}
	
	public Piece get(OrderedPair o) {
		if (!o.insideBoard())
			return null;
		return board[o.x()][o.y()];
	}
	
	public static Vector getPos(OrderedPair pos) {
		return BoardGraphics.A1.plus(pos.y * BoardGraphics.TILE_WIDTH, 0, pos.x * BoardGraphics.TILE_WIDTH);
	}
}
