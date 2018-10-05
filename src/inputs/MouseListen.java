package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import chess.Board;
import graphics.Graphics;
import math.Vector;
import shapes.Triangle;

public class MouseListen implements MouseListener {
	
	private Board b;
	public int x, y;
	public static Vector clicked;
	public static final Triangle BOARDBASE = new Triangle(-1000, 7, -1000, -1000, 7, 1000, 1000, 7, 0, null);

	public MouseListen(Board b) {
		this.b = b;
		x = 0;
		y = 0;
		clicked = null;
	}

	public void mouseClicked(MouseEvent e) {
		x = (int) ((e.getX() - 1.5) * Graphics.SCALE);
		y = (int) ((e.getY() - 3) * Graphics.SCALE);
		BOARDBASE.update();
		Vector r = BOARDBASE.getVector(x, y);
		r = r.getOldVector();
		clicked = r;
		b.click(r);
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
}
