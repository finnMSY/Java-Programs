/*
 *	===============================================================================
 *	ShapeType represents the type of a shape
 *  YOUR UPI: fmas592
 *  YOUR NAME: Finn Massey
 *	=============================================================================== */

enum ShapeType { RECTANGLE, OVAL, NESTED;
	public ShapeType next() {
		return values()[(ordinal() + 1) % values().length];
	}
}