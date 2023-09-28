package edu.iastate.cs228.hw2;

import java.util.Random;


/**
 * @author Sam Richter
 */

/**
 * The point class represents any 2 dimensional integer point and contains helper
 * functionality relating to point and point array operation.
 */
public class Point implements Comparable<Point> {

	private int x;
	private int y;
	
	public static boolean xORy;	// compare x coordinates if xORy == true and y coordinates otherwise
								// To set its value, use Point.xORy = true or false.


	/**
	 * Create a new blank point (values are by default 0)
	*/
	public Point() {}
	/**
	 * Create a new point given the x and y components
	 * 
	 * @param x the x component
	 * @param y the y component
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Create a new point using the value of another {@link Point}
	 * 
	 * @param p
	 */
	public Point(Point p) {
		x = p.getX();
		y = p.getY();
	}


	/** 
	 * Set the value of the static instance variable xORy.
	 * 
	 * @param xORy - true to select x as the primary comparison, false to select y as the primary comparison
	 */
	public static void setXorY(boolean xORy) {
		Point.xORy = xORy;
	}


	/**
	 * Get the x component of the point
	 * 
	 * @return the x integer value
	 */
	public int getX() {
		return x;
	}
	/**
	 * Get the y component of the point
	 * 
	 * @return the y integer value
	 */
	public int getY() {
		return y;
	}

	/**
	 * Compare this to another {@link Point} object. It is better to use this method rather than {@link equals()}
	 * when possible since it excludes unnecessary comparisons and edge cases.
	 * 
	 * @param p - another non null point instance
	 * @return true if the points are equal in value
	 */
	protected boolean equalsP(Point p) {
		return p != null && this.x == p.x && this.y == p.y;
	}


	/**
	 * Test whether another object is equal to this point.
	 * 
	 * @param obj - the object to test
	 * @return whether or not the object is equal to this instance
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}
	/**
	 * Compare this point with a second point q depending on the value of the static variable xORy.
	 * To compare using a definite priority use the methods compareXY() and compareYX()
	 * 
	 * @param q - the point to compare against
	 * @return the result of compareXY() if the static variable xORy is true, and the result of compareYX() otherwise - if q
	 * is null then the result is the comparison of the current instance with the point (0, 0)
	 */
	@Override
	public int compareTo(Point q) {
		if(q == null) { q = new Point(); }	// compare to (0, 0) if q is null
		return xORy ? compareXY(this, q) : compareYX(this, q);
	}
	/**
	 * Output a point in the standard form (x, y).
	 * 
	 * @return the string representation of the point
	 */
	@Override
	public String toString() {
		return String.format("(%d, %d)", this.x, this.y);
	}



	/**
	 * Raw comparison of the x component of 2 points
	 * 
	 * @param a - lhs point to compare
	 * @param b - rhs point to compare
	 * @return (a.x - b.x), which represents if a.x is bigger, equal, or less than b.x
	 */
	public static int compareX(Point a, Point b) {
		return a.x - b.x;
	}
	/**
	 * Raw comparison of the y component of 2 points
	 * 
	 * @param a - lhs point to compare
	 * @param b - rhs point to compare
	 * @return (a.y - b.y), which represents if a.y is bigger, equal, or less than b.y
	 */
	public static int compareY(Point a, Point b) {
		return a.y - b.y;
	}
	/**
	 * Compare 2 points primarily using the x component, and finally using the y component
	 * 
	 * @param a - lhs point to compare
	 * @param b - rhs point to compare
	 * @return the result of compareX(a, b) if not 0, otherwise compareY(a, b)
	 */
	public static int compareXY(Point a, Point b) {
		final int x = compareX(a, b);
		return x == 0 ? compareY(a, b) : x;
	}
	/**
	 * Compare 2 points primarily using the y component, and finally using the x component
	 * 
	 * @param a - lhs point to compare
	 * @param b - rhs point to compare
	 * @return the result of compareY(a, b) if not 0, otherwise compareX(a, b)
	 */
	public static int compareYX(Point a, Point b) {
		final int y = compareY(a, b);
		return y == 0 ? compareX(a, b) : y;
	}





	/**
	 * Generate a point with components in the range [m, M] using the random generator r
	 * 
	 * @param m - the minimum value (inclusive)
	 * @param M - the maximum value (inclusive)
	 * @param r - the random generator
	 * @return a new point with random components in the specified range
	 */
	public static Point genRange(int m, int M, Random r) {
		return new Point(
			r.nextInt(M - m + 1) + m,
			r.nextInt(M - m + 1) + m
		);
	}
	/**
	 * Generate a point with the components in the range [-R, R] using the random generator
	 * 
	 * @param R - the absolute range (inclusive) -- "radius about 0"
	 * @param r - the random generator
	 * @return a new point with random components in the specified range
	 */
	public static Point genRange(int R, Random r) {
		return genRange(-R, R, r);
	}

	/**
	 * Create a deep copy of an array of points
	 * 
	 * @param a - an array of points
	 * @return null if the input was null, otherwise the new array copy
	 */
	public static Point[] deepCopy(Point[] a) {
		if(a == null) { return null; }
		final Point[] ret = new Point[a.length];
		for(int i = 0; i < a.length; i++) {
			ret[i] = a[i] != null ? new Point(a[i]) : null;
		}
		return ret;
	}
	/**
	 * Format an array of points into a printable string representation
	 * 
	 * @param a - the array to format
	 * @return the formatted string
	 */
	public static String formatArray(Point[] a) {
		String ret = "[\n";
		for(Point p : a) {
			ret += "\t" + p.toString() + "\n";
		}
		ret += "]\n";
		return ret;
	}
	// /**
	//  * 
	//  * @param a
	//  * @return
	//  */
	// public static String formatGrid(Point[] a) {
	// 	int hx, lx, hy, ly;
	// 	for(Point p : a) {
	// 		hx = (p.x > hx ? p.x : hx) * 11 / 10;	// 10% buffer
	// 		lx = (p.x < lx ? p.x : lx) * 11 / 10;
	// 		hy = (p.y > hx ? p.y : hy) * 11 / 10;
	// 		ly = (p.y < lx ? p.y : ly) * 11 / 10;
	// 	}
	// 	final int MAX_SIZE = 25;
	// }


}
