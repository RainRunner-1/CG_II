

import org.lwjgl.util.vector.Vector4f;

public class FaceQuad {
	public Vector4f vertex 		= new Vector4f(); 
	public Vector4f texCoords 	= new Vector4f();
	public Vector4f normal 		= new Vector4f();

	public FaceQuad(Vector4f vertex, Vector4f texCoords, Vector4f normal) {
		this.vertex 	= vertex;
		this.texCoords 	= texCoords;
		this.normal 	= normal;
	}
}
