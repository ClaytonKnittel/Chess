package graphics;

import chess.Board;
import graphics.Color;
import graphics.Raw;
import math.OrderedPair;
import math.Vector;
import shapes.Shape;
import shapes.Triangle;

public class PieceGraphics extends Shape {

	Triangle[] model;				//static, nonchanging model of piece
	Triangle[] drawer;				//current model to be drawn to screen
	OrderedPair pos;
	String name;
	
	public PieceGraphics(OrderedPair pos, boolean isWhite, String name) {
		super(Color.PIECE_BLACK);
		if (isWhite)
			setColor(Color.PIECE_WHITE);
		this.name = name;
		create(BoardGraphics.TILE_WIDTH * BoardGraphics.PIECE_OCCUPANCY_CONSTANT / Raw.PIECE_WIDTH, isWhite);
		move(pos);
		super.change();
	}
	
	public void create(float scale, boolean isWhite) {
		Raw r = new Raw(name);
		model = r.getTri(getColor(), scale, !isWhite, true);
	}
	
	public void draw() {
		for (Triangle t : drawer) {
			t.draw();
		}
	}

	public void update() {
		for (int x = 0; x < model.length; x++)
			drawer[x].update();
		super.updateChange();
	}
	
	public static Triangle[] add(Triangle[] t, Vector v) {
		return add(t, v, t[0].getColor());
	}
	
	public static Triangle[] add(Triangle[] t, Vector v, Color c) {
		Triangle[] ret = new Triangle[t.length];
		for (int x = 0; x < ret.length; x++) {
			ret[x] = t[x].plus(v);
			ret[x].setColor(c);
		}
		return ret;
	}
	
	public void move(OrderedPair pos) {
		super.change();
		this.pos = pos;
		drawer = add(model, Board.getPos(pos));
	}

	public boolean isInFOV() {
		return true;
	}
}
