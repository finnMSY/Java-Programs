/*
 *	===============================================================================
 *	PathType represents the type of a path
 *  YOUR UPI: fmas592
 *  YOUR NAME: Finn Massey
 *	=============================================================================== */

enum PathType { BOUNCE, FALL;
	public PathType next() {
		return values()[(ordinal() + 1) % values().length];
	}
}
