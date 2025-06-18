

public class LineareAlgebra {	
	private LineareAlgebra() {};

	public static Vektor2D add(Vektor2D vec1, Vektor2D vec2) {
		return new Vektor2D(vec1.x + vec2.x, vec1.y + vec2.y);
	}

	public static Vektor3D add(Vektor3D vec1, Vektor3D vec2) {
		return new Vektor3D(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
	}

	public static Vektor2D sub(Vektor2D vec1, Vektor2D vec2) {
		return new Vektor2D(vec1.x - vec2.x, vec1.y - vec2.y);
	}

	public static Vektor3D sub(Vektor3D vec1, Vektor3D vec2) {
		return new Vektor3D(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
	}

	public static Vektor2D mult(Vektor2D vec, double s) {
		return new Vektor2D(vec.x * s, vec.y * s);
	}

	public static Vektor3D mult(Vektor3D vec, double s) {
		return new Vektor3D(vec.x * s, vec.y * s, vec.z * s);
	}

	public static Vektor2D mult(double s, Vektor2D vec) {
		return mult(vec, s);
	}

	public static Vektor3D mult(double s, Vektor3D vec) {
		return mult(vec, s);
	}

	public static Vektor2D div(Vektor2D vec, double s) {
		return (s != 0) ? new Vektor2D(vec.x / s, vec.y / s) : new Vektor2D(0, 0);
	}

	public static Vektor3D div(Vektor3D vec, double s) {
		return (s != 0) ? new Vektor3D(vec.x / s, vec.y / s, vec.z / s) : new Vektor3D(0, 0, 0);
	}

	public static Vektor2D div(double s, Vektor2D vec) {
		return div(vec, s);
	}

	public static Vektor3D div(double s, Vektor3D vec) {
		return div(vec, s);
	}

	public boolean isEqual(Vektor2D vec1, Vektor2D vec2) {
		return (vec1.x == vec2.x && vec1.y == vec2.y);
	}

	public boolean isEqual(Vektor3D vec1, Vektor3D vec2) {
		return (vec1.x == vec2.x && vec1.y == vec2.y && vec1.z == vec2.z);
	}

	public boolean isNotEqual(Vektor2D vec1, Vektor2D vec2) {
		return !isEqual(vec1, vec2);
	}

	public boolean isNotEqual(Vektor3D vec1, Vektor3D vec2) {
		return !isEqual(vec1, vec2);
	}

	public static double length(Vektor2D vec) {
		return Math.sqrt(lengthSquare(vec));
	}

	public static double length(Vektor3D vec) {
		return Math.sqrt(lengthSquare(vec));
	}

	public static double lengthSquare(Vektor2D vec) {
		return vec.x * vec.x + vec.y * vec.y;
	}

	public static double lengthSquare(Vektor3D vec) {
		return vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
	}

	public static double euklDistanz(Vektor2D vec, Vektor2D vec2) {
		return length(sub(vec2, vec));
	}

	public static double euklDistanz(Vektor3D vec, Vektor3D vec2) {
		return length(sub(vec2, vec));
	}

	public static Vektor2D normalize(Vektor2D vec) {
		return (vec.isNullvector()) ? new Vektor2D(mult(vec, (1.0 / vec.length() + 0.00001)))
				: new Vektor2D(div(vec, vec.length()));
	}

	public static Vektor3D normalize(Vektor3D vec) {
		return (vec.isNullvector()) ? new Vektor3D(mult(vec, (1.0 / vec.length() + 0.00001)))
				: new Vektor3D(div(vec, vec.length()));
	}

	public static Vektor3D crossProduct(Vektor3D vec1, Vektor3D vec2) {
		return new Vektor3D(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z,
				vec1.x * vec2.y - vec1.y * vec2.x);
	}

	public static double dotProduct(Vektor2D vec1, Vektor2D vec2) {
		return vec1.x * vec2.x + vec1.y * vec2.y;
	}

	public static double dotProduct(Vektor3D vec1, Vektor3D vec2) {
		return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
	}

	public static double angleRad(Vektor2D vec1, Vektor2D vec2) {
		return Math.acos(kosinusFormel(vec1, vec2));
	}

	public static double kosinusFormel(Vektor2D vec1, Vektor2D vec2) {
		return dotProduct(vec1, vec2) / (vec1.length() * vec2.length());
	}
	
	public static double kosinusFormel(Vektor3D vec1, Vektor3D vec2) {
	    return dotProduct(vec1, vec2) / (vec1.length() * vec2.length());
    }

	public static double sinusFormel(Vektor2D vec1, Vektor2D vec2) {
		return determinante(vec1, vec2) / (vec1.length() * vec2.length());
	}

	public static double angleRad(Vektor3D vec1, Vektor3D vec2) {
		return Math.acos(dotProduct(vec1, vec2) / (vec1.length() * vec2.length()));
	}

	public static double angleRadFast(Vektor2D vec1, Vektor2D vec2) {
		return Math.acos(dotProduct(vec1, vec2) / Math.sqrt(vec1.lengthSquare() * vec2.lengthSquare()));
	}

	public static double angleRadFast(Vektor3D vec1, Vektor3D vec2) {
		return Math.acos(dotProduct(vec1, vec2) / Math.sqrt(vec1.lengthSquare() * vec2.lengthSquare()));
	}

	public static double angleDegree(Vektor2D vec1, Vektor2D vec2) {
		return radToDegree(Math.acos(dotProduct(vec1, vec2) / (vec1.length() * vec2.length())));
	}

	public static double angleDegree(Vektor3D vec1, Vektor3D vec2) {
		return radToDegree(Math.acos(dotProduct(vec1, vec2) / (vec1.length() * vec2.length())));
	}

	public static double determinante(Vektor2D v, Vektor2D w) {
		return v.x * w.y - v.y * w.x;
	}

	public static double radToDegree(double rad) {
		return 180 * rad / Math.PI;
	}

	public static double degreeToRad(double degree) {
		return Math.PI * degree / 180;
	}

	public static Vektor2D abs(Vektor2D vec) {
		return new Vektor2D(Math.abs(vec.x), Math.abs(vec.y));
	}

	public static Vektor3D abs(Vektor3D vec) {
		return new Vektor3D(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
	}

	public static Vektor2D truncate(Vektor2D vec, double max) {
		Vektor2D newVec = new Vektor2D(vec);
		if (newVec.length() > max) {
			newVec.normalize();
			newVec.mult(max);
		}
		return newVec;
	}

	public static Vektor2D rotate(Vektor2D vec, double degree) {
		double rad = degreeToRad(degree);
		return (new Vektor2D(Math.cos(rad) * vec.x - Math.sin(rad) * vec.y,
				Math.sin(rad) * vec.x + Math.cos(rad) * vec.y));
	}

	public static Vektor2D senkrechte(Vektor2D vec) {
		Vektor2D newVec = new Vektor2D(vec);
		return rotate(newVec, 90);
	}
	
    public static double clamp(double x, double min, double max) {
       if (x < min)
          return min;
       else if (x > max)
          return max;
       return x;
    }
    
    public static double barycentricInterpolation(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C, double m_a, double m_b, double m_c) {
        double denom = 1./((B.x-A.x) * (C.y-B.y) - (B.y-A.y) * (C.x-B.x));
        double u = ((B.x-P.x) * (C.y-P.y) - (C.x-P.x) * (B.y-P.y)) * denom; 
        double v = ((C.x-P.x) * (A.y-P.y) - (A.x-P.x) * (C.y-P.y)) * denom; 
        double w = 1. - u - v;
        return u*m_a+v*m_b+w*m_c;
    }

	public static void show(Vektor2D vec) {
		System.out.println("(" + vec.x + ", " + vec.y + ")");
	}

	public static void show(Vektor3D vec) {
		System.out.println("(" + vec.x + ", " + vec.y + ", " + vec.z + ")");
	}
}
