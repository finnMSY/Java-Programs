/*
 *	===============================================================================
 *	RectangleShape.java : A shape that is a rectangle, and a place where all the methods relating to an Rectangle can be stored.
 *  YOUR UPI: fmas592
 *  YOUR ID: 544773353
 *  YOUR NAME: Finn Massey
 *	=============================================================================== */
import java.awt.*;
class RectangleShape extends Shape {
    public RectangleShape() {}
	public RectangleShape(int x, int y, int w, int h, int mw, int mh, Color c, PathType pt, String text) {
		super(x ,y ,w, h ,mw ,mh, c, pt, text);
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
	@Override
	public boolean contains(Point mousePt) {
		return (x <= mousePt.x && mousePt.x <= (x + width + 1)	&&	y <= mousePt.y && mousePt.y <= (y + height + 1));
	}
}