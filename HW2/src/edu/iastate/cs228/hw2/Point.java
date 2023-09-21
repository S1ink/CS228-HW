package edu.iastate.cs228.hw2;

/**
 *  
 * @author
 *
 */

public class Point implements Comparable<Point>
{
	private int x;
	private int y;
	
	public static boolean xORy;	// compare x coordinates if xORy == true and y coordinates otherwise 
								// To set its value, use Point.xORy = true or false. 


	public Point() {	// default constructor
		// x and y get default value 0
	}
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Point(Point p) {		// copy constructor
		x = p.getX();
		y = p.getY();
	}


	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	/** 
	 * Set the value of the static instance variable xORy.
	 * @param xORy
	 */
	public static void setXorY(boolean xORy) {
		Point.xORy = xORy;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}

	protected boolean fastEquals(Point p) {
		return p != null && this.x == p.x && this.y == p.y;
	}

	/**
	 * Compare this point with a second point q depending on the value of the static variable xORy 
	 * @param 	q
	 * @return  -1  if (xORy == true && (this.x < q.x || (this.x == q.x && this.y < q.y)))
	 *                || (xORy == false && (this.y < q.y || (this.y == q.y && this.x < q.x)))
	 * 		    0   if this.x == q.x && this.y == q.y)
	 * 			1	otherwise
	 */
	public int compareTo(Point q) {
		if(q != null) {
			if(this.fastEquals(q)) { return 0; }
			if(xORy) {
				
			} else {

			}
		}
		return -2;
	}


	/**
	 * Output a point in the standard form (x, y). 
	 */
	@Override
	public String toString() {
		return String.format("(%d, %d)", this.x, this.y);
	}




	public static int compareX(Point a, Point b) {
		return (int)Math.signum(a.x - b.x);
	}
	public static int compareY(Point a, Point b) {
		return (int)Math.signum(a.y - b.y);
	}

	/**
	 * 
	 * @param a
	 * @return
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
	 * 
	 * @param a
	 * @return
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
	// 		hx = p.x > hx ? p.x : hx;
	// 		lx = p.x < lx ? p.x : lx;
	// 		hy = p.y > hx ? p.y : hy;
	// 		ly = p.y < lx ? p.y : ly;
	// 	}
	// }


}
