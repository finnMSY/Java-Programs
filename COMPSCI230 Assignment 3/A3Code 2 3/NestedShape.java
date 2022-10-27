/*
 * ==========================================================================================
 * NestedShape.java : A shape that is an outline of a rectangle and contains smaller inner shapes.
 * YOUR UPI: fmas592
 * YOUR NAME: Finn Massey
 * ==========================================================================================
 */

import java.awt.*;
import java.util.*;
public class NestedShape extends RectangleShape {
    private ArrayList<Shape> innerShapes = new ArrayList<Shape>();

    public NestedShape() {
        super();
        createInnerShape(0, 0, this.width / 2, this.height / 2, this.color, PathType.BOUNCE, ShapeType.RECTANGLE);
    }

    public NestedShape(int x, int y, int width, int height, int panelWidth, int panelHeight, Color color, PathType path) {
        super(x, y, width, height, panelWidth, panelHeight, color, path);
        createInnerShape(0, 0, width / 2, height / 2, color, PathType.BOUNCE, ShapeType.RECTANGLE);
    }

    public NestedShape(int width, int height) {
        super.width = width;
        super.height = height;
        super.color = Color.black;
    }

    public Shape createInnerShape(int x, int y, int w, int h, Color c, PathType pt, ShapeType st) {
        Shape shape;
        if (st == ShapeType.RECTANGLE) {
            shape = new RectangleShape(x, y, w, h, this.width, this.height, c, pt);
        }
        else if (st == ShapeType.OVAL) {
            shape = new OvalShape(x, y, w, h, this.width, this.height, c, pt);
        }
        else {
            shape = new NestedShape(x, y, w, h, this.width, this.height, c, pt);
        }
        shape.setParent(this);
        innerShapes.add(shape);
        return shape;
    }

    public Shape getInnerShapeAt(int index) {
        return innerShapes.get(index);
    }

    public int getSize() {
        return innerShapes.size();
    }

    public int indexOf(Shape s) {
        return innerShapes.indexOf(s);
    }

    public void addInnerShape(Shape s) {
        innerShapes.add(s);
        s.setParent(this);
    }

    public void removeInnerShape(Shape s) {
        innerShapes.remove(s);
        s.setParent(null);
    }

    public void removeInnerShapeAt(int index) {
        Shape s = getInnerShapeAt(index);
        s.setParent(null);
        innerShapes.remove(s);
    }

    public ArrayList<Shape> getAllInnerShapes() {
        return innerShapes;
    }

    @Override
    public void setColor(Color c) {
        super.color = c;
        for (Shape s : innerShapes) {
            s.setColor(c);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(x, y, width, height);
        g.translate(x, y);

        for (Shape s : innerShapes) {
            s.draw(g);
        }
        g.translate(-x, -y);
    }

    @Override
    public void move() {
        super.move();
        for (Shape s : innerShapes) {
            s.move();
        }
    }
}