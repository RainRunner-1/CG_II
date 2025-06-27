import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Model {
    public List<Vector3f> vertices = new ArrayList<>();
    public List<Vector3f> normals = new ArrayList<>();
    public List<Vector2f> texCoords = new ArrayList<>();
    public List<FaceTriangle> faces = new ArrayList<>();
    public List<FaceQuad> facesQuads = new ArrayList<>();
    public float size = 1.0f;
    
    // FaceQuad als innere Klasse mit korrigiertem Konstruktor
    public static class FaceQuad {
        public Vector4f vertex;
        public Vector4f texCoords;
        public Vector4f normal;
        
        // KORRIGIERTER Konstruktor - nimmt Vector4f Parameter
        public FaceQuad(Vector4f vertex, Vector4f texCoords, Vector4f normal) {
            this.vertex = vertex;
            this.texCoords = texCoords;
            this.normal = normal;
        }
    }
}
