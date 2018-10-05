package graphics;

import java.util.ArrayList;

import chess.Board;
import director.Director;
import graphics.Color;
import graphics.Raw;
import math.OrderedPair;
import math.Vector;
import shapes.Shape;
import shapes.Triangle;

public class BoardGraphics extends Shape {

	private Triangle[] board;
	public static final float TILE_WIDTH = 10;
	public static final float PIECE_OCCUPANCY_CONSTANT = 0.8f;		//piece width / tile width (default: 0.8f)
	public static final Vector POS = new Vector(0, 0, 0);
	public static final Vector A1 = new Vector(-3.5f * TILE_WIDTH, 7, -3.5f * TILE_WIDTH).plus(POS);
	public static final Triangle[] tileHighlightModel = (new Raw("Highlight")).getTri(Color.YELLOW, TILE_WIDTH, false, true);
	private ArrayList<Triangle[]> tileHighlightDrawers;
	private Vector clickerPos;
	private Board b;
	
	public BoardGraphics(Color c, Board b) {
		super(c);				//Color of outside of board
		tileHighlightDrawers = new ArrayList<Triangle[]>();
		this.b = b;
		create();
	}
	
	public void create() {
		Triangle[] tBase = (new Raw("Board base")).getTri(getColor(), 10, false, true);
		tBase = PieceGraphics.add(tBase, POS);
		
		Triangle[] tBlack = new Triangle[64];
		Triangle[] tWhite = new Triangle[64];
		Vector pos0 = A1.minus(TILE_WIDTH / 2, 0, TILE_WIDTH / 2);
		Vector pos = null;
		int xx = 0;
		for (int x = 0; x < 64; x++) {
			pos = pos0.plus(new Vector((x % 8) * TILE_WIDTH, 0, (x / 8) * TILE_WIDTH));
			if ((((x / 8) % 2) + x % 8) % 2 == 0) {
				if ((x / 8) % 2 == 0)
					xx = x;
				else
					xx = x - 1;
				tBlack[xx] = new Triangle(pos, pos.plus(TILE_WIDTH, 0, TILE_WIDTH), pos.plus(TILE_WIDTH, 0, 0), Color.SOFT_BLACK);
				tBlack[xx + 1] = new Triangle(pos, pos.plus(0, 0, TILE_WIDTH), pos.plus(TILE_WIDTH, 0, TILE_WIDTH), Color.SOFT_BLACK);
			} else {
				if ((x / 8) % 2 == 0)
					xx = x - 1;
				else
					xx = x;
				tWhite[xx] = new Triangle(pos, pos.plus(TILE_WIDTH, 0, TILE_WIDTH), pos.plus(TILE_WIDTH, 0, 0), Color.SOFT_WHITE);
				tWhite[xx + 1] = new Triangle(pos, pos.plus(0, 0, TILE_WIDTH), pos.plus(TILE_WIDTH, 0, TILE_WIDTH), Color.SOFT_WHITE);
			}
		}
		board = new Triangle[tBase.length + tBlack.length + tWhite.length];
		for (int x = 0; x < board.length; x++) {
			if (x < tBase.length)
				board[x] = tBase[x];
			else if (x < tBase.length + tBlack.length)
				board[x] = tBlack[x - tBase.length];
			else
				board[x] = tWhite[x - tBase.length - tBlack.length];
		}
		Director.world.add(this);
	}
	
	public static Triangle[] add(Triangle[] t, ArrayList<Triangle[]> r) {
		if (r.size() == 0) return t;
		Triangle[] ret = new Triangle[t.length + r.size() * r.get(0).length];
		for (int x = 0; x < ret.length; x++) {
			if (x < t.length) {
					ret[x] = t[x];
			}
			else {
					ret[x] = r.get((x - t.length) / r.get(0).length)[x - t.length - ((x - t.length) / r.get(0).length) * r.get(0).length];
			}
		}
		return ret;
	}
	
	public void updateClicker(Vector v) {
		int x = getTile(v);
		if (!b.canMove(x)) {
			clear();
			return;
		}
		OrderedPair pos = OrderedPair.toPos(x);
		clickerPos = Board.getPos(pos);
		b.select(pos);
		if (b.pieceSelected()) {
			tileHighlightDrawers.add(PieceGraphics.add(tileHighlightModel, clickerPos));
			if (b.cheats()) {
				for (int z = 0; z < 64; z++) {
					if (b.getSelectedPiece().canMove(z))
						tileHighlightDrawers.add(PieceGraphics.add(tileHighlightModel, Board.getPos(new OrderedPair(z)), Color.GREEN));
				}
			}
		} else clear();
	}
	
	public void clear() {
		clickerPos = null;
		tileHighlightDrawers.clear();
	}

	public void update() {
		for (int x = 0; x < board.length; x++) {
			board[x].update();
		}
		for (int x = 0; x < tileHighlightDrawers.size(); x++) {
			for (int y = 0; y < tileHighlightDrawers.get(x).length; y++)
				tileHighlightDrawers.get(x)[y].update();
		}
	}
	
	public void draw() {
		for (Triangle t : getBoard())
			t.draw();
	}
	
	private Triangle[] getBoard() {
		return add(board, tileHighlightDrawers);
	}
	
	/**
	 * 
	 * @param v: Drawn with respect to origin, not camera
	 * @return integer position of tile clicked, -1 if no tile clicked
	 */
	public static int getTile(Vector v) {
		int x = (int) ((v.getX() - BoardGraphics.A1.getX() + (TILE_WIDTH / 2f)) / TILE_WIDTH);
		int z = (int) ((v.getZ() - BoardGraphics.A1.getZ() + (TILE_WIDTH / 2f)) / TILE_WIDTH);
		if (x >= 0 && x < 8 && z >= 0 && z < 8)
			return x * 8 + z;
		return -1;
	}

	public boolean isInFOV() {
		return true;
	}

}
