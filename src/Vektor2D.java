

public class Vektor2D {
	public double x, y;

	public Vektor2D() {
		this(0, 0);
	}

	public Vektor2D(double x, double y) {
		setX(x);
		setY(y);
	}

	public Vektor2D(Vektor2D vec) {
		this(vec.x, vec.y);
	}

	public Vektor2D(double x, double y, double x2, double y2) {
		this(x2 - x, y2 - y);
	}

	public Vektor2D(Vektor2D a, Vektor2D b) {
		this(b.x - a.x, b.y - a.y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setPosition(Vektor2D vec) {
		setX(vec.x);
		setY(vec.y);
	}

	public boolean isNullvector() {
		return (x == 0 && y == 0);
	}

	public void add(Vektor2D vec) {
		x += vec.x;
		y += vec.y;
	}

	public void sub(Vektor2D vec) {
		x -= vec.x;
		y -= vec.y;
	}

	public void mult(double s) {
		x *= s;
		y *= s;
	}
	
	public boolean div(double s) {
		if (s!=0) {
			x /= s;
			y /= s;
			return true;
		}
		return false;
	}

	public boolean isEqual(Vektor2D vec) {
		return (x == vec.x && y == vec.y);
	}

	public boolean isNotEqual(Vektor2D vec) {
		return !isEqual(vec);
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public double lengthSquare() {
		return x * x + y * y;
	}

	public void normalize() {
		if (this.isNullvector())
			setPosition(LineareAlgebra.mult(this, (1.0 / this.length() + 0.00001)));
		else			
			setPosition(LineareAlgebra.div(this, this.length()));
	}
	
	public void truncate(double max) {
		if (length() > max) {
			normalize();
			mult(max);
		}
	}
	
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}

	public void show() {
		System.out.println(toString());
	}
	
	public static void main(String[] args) {
		Vektor2D a = new Vektor2D(4, 3);
		Vektor2D b = new Vektor2D(2, -1);
		
		System.out.println(a);
		a.add(b);
		System.out.println(a);
	}
}
