/*
 *	===============================================================================
 *	OvalShape.java : A shape that is an oval, and a place where all the methods relating to an Oval can be stored.
 *  YOUR UPI: fmas592
 *  YOUR ID: 544773353
 *  YOUR NAME: Finn Massey
 *	=============================================================================== */
import java.awt.*;
class OvalShape extends Shape {
	public OvalShape() {}
	public OvalShape(int x, int y, int w, int h, int mw, int mh, Color c, PathType pt, String text) {
		super(x ,y ,w, h ,mw ,mh, c, pt, text);
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, width, height);
	}
	@Override
	public boolean contains(Point mousePt) {
		double dx, dy;
		Point EndPt = new Point(x + width, y + height);
		dx = (2 * mousePt.x - x - EndPt.x) / (double) width;
		dy = (2 * mousePt.y - y - EndPt.y) / (double) height;
		return dx * dx + dy * dy < 1.0;
	}
}