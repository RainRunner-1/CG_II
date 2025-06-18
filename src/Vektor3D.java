

public class Vektor3D {
	public double x, y, z;

	public Vektor3D() {
		this(0, 0, 0);
	}

	public Vektor3D(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public Vektor3D(Vektor3D vec) {
		this(vec.x, vec.y, vec.z);
	}

	public Vektor3D(double x, double y, double z, double x2, double y2, double z2) {
		this(x2 - x, y2 - y, z2 - z);
	}

	public Vektor3D(Vektor3D a, Vektor3D b) {
		this(b.x - a.x, b.y - a.y, b.z - a.z);
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return z;
	}

	public void setPosition(Vektor3D vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public boolean isNullvector() {
		return (x == 0 && y == 0 && z == 0);
	}

	public void add(Vektor3D vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
	}

	public void sub(Vektor3D vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
	}

	public void mult(double s) {
		x *= s;
		y *= s;
		z *= s;
	}

	public boolean isEqual(Vektor3D vec) {
		return (x == vec.x && y == vec.y && z == vec.z);
	}

	public boolean isNotEqual(Vektor3D vec) {
		return !isEqual(vec);
	}

	public double length() {
		return Math.sqrt(x*x + y*y + z*z);
	}

	public double lengthSquare() {
		return x*x + y*y + z*z;
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
		return ("("+x+", "+y+", "+z+")");
	}

	public void show() {
		System.out.println(toString());
	}
}