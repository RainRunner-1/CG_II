public class Vector3f {
    public float x, y, z;
    
    public Vector3f() {
        this(0, 0, 0);
    }
    
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void set(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public void add(Vector3f v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }
    
    public void subtract(Vector3f v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }
    
    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }
    
    public float length() {
        return (float)Math.sqrt(x*x + y*y + z*z);
    }
    
    public float distance(Vector3f other) {
        float dx = x - other.x;
        float dy = y - other.y;
        float dz = z - other.z;
        return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    
    public static Vector3f add(Vector3f a, Vector3f b, Vector3f result) {
        if (result == null) result = new Vector3f();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
        return result;
    }
    
    public static Vector3f subtract(Vector3f a, Vector3f b, Vector3f result) {
        if (result == null) result = new Vector3f();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
        return result;
    }
    
    public void normalize() {
        float len = length();
        if (len > 0) {
            x /= len;
            y /= len;
            z /= len;
        }
    }
    
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}