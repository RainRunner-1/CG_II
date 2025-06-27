

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_QUAD_STRIP;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import LineareAlgebra;
import Vektor2D;
import Model;
import Weg2DDynamisch;
import Model.FaceQuad;

// POGL = "Primitives of OpenGL" 
public class POGL {
	private POGL() {
	}



	public static void clearBackgroundWithColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public static void setBackGroundColorClearDepth(float a, float b, float c) {
		glClearColor(a, b, c, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void renderKreis(float x, float y, float step, float radius) {
		glBegin(GL_TRIANGLE_FAN);
		glVertex2f(x, y);
		for (int angle = 0; angle < 360; angle += step)
			glVertex2f(x + (float) Math.sin(angle) * radius, y + (float) Math.cos(angle) * radius);
		glEnd();
	}

	public static void renderViereck() {
		glBegin(GL_QUADS);
		glVertex3f(-1, -1, 0);
		glVertex3f(1, -1, 0);
		glVertex3f(1, 1, 0);
		glVertex3f(-1, 1, 0);
		glEnd();
	}
	
	public static void renderKugel(float r, int h, int v) {
		int i, j;
		for (i = 0; i <= h; i++) {
			float hor1 = (float) (Math.PI * (-0.5 + (double) (i-1)/h));
			float z1   = (float) Math.sin(hor1);
			float zr1  = (float) Math.cos(hor1);

			float hor2 = (float) (Math.PI * (-0.5 + (double) i/h));
			float z2   = (float) Math.sin(hor2);
			float zr2  = (float) Math.cos(hor2);

			glBegin(GL_QUAD_STRIP);
			for (j = 0; j <= v; j++) {
				float ver = (float) (2 * Math.PI * (double) (j-1)/v);
				float x   = (float) Math.cos(ver);
				float y   = (float) Math.sin(ver);
				glNormal3f(x * zr1, y * zr1, z1);
				glVertex3f(r * x * zr1, r * y * zr1, r * z1);
				glNormal3f(x * zr2, y * zr2, z2);
				glVertex3f(r * x * zr2, r * y * zr2, r * z2);
			}
			glEnd();
		}
	}
	
	public static void renderPyramide() {
		glBegin(GL_TRIANGLES);
		glVertex3f( 1.0f,  1.0f, -1.0f);
		glVertex3f(-1.0f,  1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);

		glVertex3f( 1.0f,  1.0f, -1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f( 1.0f, -1.0f, -1.0f);

		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f( 0.0f,  0.0f,  1.0f);
		glVertex3f(-1.0f,  1.0f, -1.0f);

		glVertex3f( 0.0f,  0.0f,  1.0f);
		glVertex3f(-1.0f, -1.0f, -1.0f);
		glVertex3f( 1.0f, -1.0f, -1.0f);

		glVertex3f(0.0f,  0.0f,  1.0f);
		glVertex3f(1.0f, -1.0f, -1.0f);
		glVertex3f(1.0f,  1.0f, -1.0f);

		glVertex3f( 0.0f, 0.0f,  1.0f);
		glVertex3f( 1.0f, 1.0f, -1.0f);
		glVertex3f(-1.0f, 1.0f, -1.0f);
		glEnd();
	}
	
	public static void renderEgg(int n) {
		double step = (double)360 / n;
	
		for (int i = 0; i < n; i++) {
			double x = 0.8*Math.cos((step*i)*(Math.PI/180));
			double y = Math.sin((step*i)*(Math.PI/180));
			double x_next = 0.8*Math.cos((step*(i+1))*(Math.PI/180));
			double y_next = Math.sin((step*(i+1))*(Math.PI/180));
			
			glBegin(GL_TRIANGLES);
			glVertex3d(0,0,0);
			glVertex3d(x,y,0);
			glVertex3d(x_next, y_next, 0);
		
		glEnd();
		}
	}
	
	public static void renderEi(float x, float y, float z, float step, float radius) {
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(x, y, z);

		boolean farbe = true,c=true;
		double toto;float a=0;
		for (float i = -1; i < 1; i += 0.01) {
			for (int angle = 0, j2 = 0; angle <= 360; angle += step, j2++) {
				toto = 3.14159 * angle / 180.;
				glVertex3f((x + (float) ((Math.sin(toto) * radius) * Math.cos(Math.asin(i)))),
						(y + (float) (Math.cos(toto) * radius * Math.cos(Math.asin(i)))), i * 1.5f);
				if (j2 >= 3) {
					if (farbe) {
						if(a>=1) c=false;
						if(a<=0) c=true;
						if(c)a+=0.01;
						else a-=0.01;
						glColor3d(0, 1*a, 1*(1-a));
						farbe = !farbe;
						j2 = 0;
					} else {
						glColor3d(0, 0,1);
						farbe = !farbe;
					}
				} else {
					glColor3d(0, 0, 1);
				}
				
			}


		}
		glEnd();
	}
	
	public static void renderEggDot(int n, double pos_x, double pos_y, double scale,int layer) {
		double step = (double)360 / n;
		
		
		for (int i = 0; i < n; i++) {
			double x = scale*(0.8*Math.cos((step*i)*(Math.PI/180)));
			double y = scale*(Math.sin((step*i)*(Math.PI/180)));
			double x_next = scale*(0.8*Math.cos((step*(i+1))*(Math.PI/180)));
			double y_next = scale*(Math.sin((step*(i+1))*(Math.PI/180)));
			
			glBegin(GL_TRIANGLES);
			glVertex3d(pos_x,pos_y,(0.0001+0.0002*n));
			glVertex3d((x+pos_x),(y+pos_y),(0.0001+0.0002*n));
			glVertex3d((x_next+pos_x), (y_next+pos_y), (0.0001+0.0002*n));
			glEnd();
			
			glBegin(GL_TRIANGLES);
			glVertex3d(pos_x,pos_y,(-0.0001+-0.0002*n));
			glVertex3d((x+pos_x),(y+pos_y),(-0.0001+-0.0002*n));
			glVertex3d((x_next+pos_x), (y_next+pos_y), (-0.0001+-0.0002*n));
			glEnd();
		}
	}

	public static void renderWehendeFlaecheKonstruktion(double t) {
		glBegin(GL_QUADS);
		double periode = 20, amplitude = 0.03, step = 0.1;
		float r = 0.5f, g = 0.0f, b = 0.0f;
		double bl, br, tr, tl;

		double flag = 1;
		for (double y = -0.5; y < 0.5; y += step) {
			for (double x = -0.5; x < 0.5; x += step) {
				bl = Math.sin((t * 10 + x * y * periode)) * amplitude;
				br = Math.sin((t * 10 + (x + step) * y * periode)) * amplitude;
				tr = Math.sin((t * 10 + (x + step) * (y + step) * periode)) * amplitude;
				tl = Math.sin((t * 10 + x * (y + step) * periode)) * amplitude;

				glColor3d(r + bl + flag * step, g + bl + flag * step, b + bl + flag * step);
				glVertex2d(x + bl, y + bl);
				glColor3d(r + br, g + br, b + br);
				glVertex2d(x + step + br, y + br);
				glColor3d(r + tr, g + tr, b + tr);
				glVertex2d(x + step + tr, y + step + tr);
				glColor3d(r + tl, g + tl, b + tl);
				glVertex2d(x + tl, y + step + tl);

				flag *= -1;
			}
		}
		glEnd();
	}

	public static void renderWehendeFlaeche(double t) {
		glBegin(GL_QUADS);
		double periode = 15, amplitude = 0.03, step = 0.01;
		float r = 0.0f, g = 0.5f, b = 0.0f;
		double bl, br, tl, tr;

		for (double y = -0.5; y < 0.5; y += step) {
			for (double x = -0.5; x < 0.5; x += step) {
				bl = Math.sin((t * 10 + x * y * periode)) * amplitude;
				br = Math.sin((t * 10 + (x + step) * y * periode)) * amplitude;
				tr = Math.sin((t * 10 + (x + step) * (y + step) * periode)) * amplitude;
				tl = Math.sin((t * 10 + x * (y + step) * periode)) * amplitude;

				glColor3d(r + bl, g + bl, b + bl);
				glVertex2d(x + bl, y + bl);
				glColor3d(r + br, g + br, b + br);
				glVertex2d(x + step + br, y + br);
				glColor3d(r + tr, g + tr, b + tr);
				glVertex2d(x + step + tr, y + step + tr);
				glColor3d(r + tl, g + tl, b + tl);
				glVertex2d(x + tl, y + step + tl);
			}
		}
		glEnd();
	}


	public static Model loadModel(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model model = new Model();
		String line;
		String[] lineElements;
		float x, y, z;
		Vector3f vertexIndices 		= null;
		Vector3f texCoordsIndices 	= null;
		Vector3f normalIndices 		= null;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vt ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				model.texCoords.add(new Vector2f(x, y));
			} else if (line.startsWith("f ")) {
				vertexIndices 		= null;
				texCoordsIndices 	= null;
				normalIndices 		= null;

				lineElements = line.split(" ");
				if (line.contains("/") && lineElements[1].split("/").length > 1) {
					vertexIndices = new Vector3f(Float.valueOf(lineElements[1].split("/")[0]),
							Float.valueOf(lineElements[2].split("/")[0]),
							Float.valueOf(lineElements[3].split("/")[0]));
					texCoordsIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]),
							Float.valueOf(lineElements[2].split("/")[1]),
							Float.valueOf(lineElements[3].split("/")[1]));
					if (lineElements[1].split("/").length == 3) {
						normalIndices = new Vector3f(Float.valueOf(lineElements[1].split("/")[2]),
								Float.valueOf(lineElements[2].split("/")[2]),
								Float.valueOf(lineElements[3].split("/")[2]));
					}
				} else {
					// nur drei Vertices f�r ein Dreieck vorhanden
					vertexIndices = new Vector3f(
							Float.valueOf(lineElements[1]), 
							Float.valueOf(lineElements[2]),
							Float.valueOf(lineElements[3]));
				}
				model.faces.add(new FaceTriangle(vertexIndices, texCoordsIndices, normalIndices));
			}
		}
		reader.close();
		return model;
	}
	
	public static Model loadModelVierecke(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model model = new Model();
		String line;
		String[] lineElements;
		float x, y, z;
		Vector4f vertexIndices = null;
		Vector4f texCoordsIndices = null;
		Vector4f normalIndices = null;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) { 
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vt ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				model.texCoords.add(new Vector2f(x, y));
			} else if (line.startsWith("f ")) {
				vertexIndices = null;
				texCoordsIndices = null;
				normalIndices = null;

				lineElements = line.split(" ");
				if (line.contains("/") && lineElements[1].split("/").length > 1) {
					vertexIndices = new Vector4f(
							Float.valueOf(lineElements[1].split("/")[0]),
							Float.valueOf(lineElements[2].split("/")[0]), 
							Float.valueOf(lineElements[3].split("/")[0]),
							Float.valueOf(lineElements[4].split("/")[0]));

					texCoordsIndices = new Vector4f(
							Float.valueOf(line.split(" ")[1].split("/")[1]),
							Float.valueOf(lineElements[2].split("/")[1]), 
							Float.valueOf(lineElements[3].split("/")[1]),
							Float.valueOf(lineElements[4].split("/")[1]));

					if (lineElements[1].split("/").length == 3) {
						normalIndices = new Vector4f(
								Float.valueOf(lineElements[1].split("/")[2]),
								Float.valueOf(lineElements[2].split("/")[2]),
								Float.valueOf(lineElements[3].split("/")[2]),
								Float.valueOf(lineElements[4].split("/")[2]));
					}
				} else {
					// nur vier Vertices f�r ein Viereck vorhanden
					vertexIndices = new Vector4f(
							Float.valueOf(lineElements[1]), 
							Float.valueOf(lineElements[2]),
							Float.valueOf(lineElements[3]),
							Float.valueOf(lineElements[4]));
				}
				model.facesQuads.add(new FaceQuad(vertexIndices, texCoordsIndices, normalIndices));
			}
		}
		reader.close();
		return model;
	}

	public static void renderObject(Model model) {
		glBegin(GL_TRIANGLES);
		for (FaceTriangle face : model.faces) {
			if (face.normal != null) {
				Vector3f n1 = model.normals.get((int) face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
			}
			if (face.texCoords != null) {
				Vector2f t1 = model.texCoords.get((int) face.texCoords.x - 1);
				glTexCoord2f(t1.x, t1.y);
			}
			Vector3f v1 = model.vertices.get((int) face.vertex.x - 1);
			glVertex3f(v1.x, v1.y, v1.z);
			
			if (face.normal != null) {
				Vector3f n2 = model.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
			}
			if (face.texCoords != null) {
				Vector2f t2 = model.texCoords.get((int) face.texCoords.y - 1);
				glTexCoord2f(t2.x, t2.y);
			}
			Vector3f v2 = model.vertices.get((int) face.vertex.y - 1);
			glVertex3f(v2.x, v2.y, v2.z);
			
			if (face.normal != null) {
				Vector3f n3 = model.normals.get((int) face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
			}
			if (face.texCoords != null) {
				Vector2f t3 = model.texCoords.get((int) face.texCoords.z - 1);
				glTexCoord2f(t3.x, t3.y);
			}
			Vector3f v3 = model.vertices.get((int) face.vertex.z - 1);
			glVertex3f(v3.x, v3.y, v3.z);
		}
		glEnd();
		glPopMatrix();
	}
	
	public static void renderObjectVierecke(Model model) {
		glBegin(GL_QUADS);
		for (FaceQuad face : model.facesQuads) {
			if (face.normal != null) {
				Vector3f n1 = model.normals.get((int) face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
			}
			if (face.texCoords != null) {
				Vector2f t1 = model.texCoords.get((int) face.texCoords.x - 1);
				glTexCoord2f(t1.x, t1.y);
			}
			Vector3f v1 = model.vertices.get((int) face.vertex.x - 1);
			glVertex3f(v1.x, v1.y, v1.z);

			if (face.normal != null) {
				Vector3f n2 = model.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
			}
			if (face.texCoords != null) {
				Vector2f t2 = model.texCoords.get((int) face.texCoords.y - 1);
				glTexCoord2f(t2.x, t2.y);
			}
			Vector3f v2 = model.vertices.get((int) face.vertex.y - 1);
			glVertex3f(v2.x, v2.y, v2.z);

			if (face.normal != null) {
				Vector3f n3 = model.normals.get((int) face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
			}
			if (face.texCoords != null) {
				Vector2f t3 = model.texCoords.get((int) face.texCoords.z - 1);
				glTexCoord2f(t3.x, t3.y);
			}
			Vector3f v3 = model.vertices.get((int) face.vertex.z - 1);
			glVertex3f(v3.x, v3.y, v3.z);

			if (face.normal != null) {
				Vector3f n4 = model.normals.get((int) face.normal.w - 1);
				glNormal3f(n4.x, n4.y, n4.z);
			}
			if (face.texCoords != null) {
				Vector2f t4 = model.texCoords.get((int) face.texCoords.w - 1);
				glTexCoord2f(t4.x, t4.y);
			}
			Vector3f v4 = model.vertices.get((int) face.vertex.w - 1);
			glVertex3f(v4.x, v4.y, v4.z);

		}
		glEnd();
		glPopMatrix();
	}
	
	public static void renderObjectWithPath(float x, float y, float r, float g, float b, float a, int radius, Weg2DDynamisch path) {
		for (int j = path.getSize()-1; j >= 0 ; j--) {
			float anteil = 1-((float)j/path.getSize());
			glColor4f(r*anteil, g*anteil, b*anteil, 1f);
			renderKreis((float)path.getElement(j).x, (float)path.getElement(j).y, 5, radius);
		}

		glColor4f(r, g, b, a);
		renderKreis(x, y, 5, radius);
	}
	
	public static void renderPfeil(float x, float y, int off, float winkel, int size) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glRotatef(winkel, 0, 0, 1);
		glTranslated(off, 0, 0);
		glScaled(size, size, size);
		
		glBegin(GL_LINES);
		glVertex3d(  0f,  0f, 0);
		glVertex3d(-off/15., 0, 0);
		glEnd();

        glBegin(GL_TRIANGLES);
        glVertex3d(  0f,  .2f, 0);
        glVertex3d(  0f, -.2f, 0);
        glVertex3d( .5f,   0f, 0);
        glEnd();    		
	}
	
	public static void renderObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glColor4f(1, 1, 1, 1);
		renderKreis(0, 0, 5, radius);
		glColor4f(0, 0, 0, 1);
		renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen gr��eren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, m�ssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 1, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}
	
	public static void renderSwarmObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glColor4f(1, 1, 1, 1);
		renderKreis(0, 0, 5, radius);
		glColor4f(0, 0, 0, 1);
		renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen gr��eren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, m�ssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 1, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}
	
	// L�sung von Dustin Heyer
	public static void renderKugel2() {  
		int i, j;
		double r = 1;
		int lats = 30;
		int longs = 30;
		for (i = 0; i <= lats; i++) {
			double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
			double z0 = Math.sin(lat0);
			double zr0 = Math.cos(lat0);

			double lat1 = Math.PI * (-0.5 + (double) i / lats);
			double z1 = Math.sin(lat1);
			double zr1 = Math.cos(lat1);

			glBegin(GL_QUAD_STRIP);
				for (j = 0; j <= longs; j++) {
					double lng = 2 * Math.PI * (double) (j - 1) / longs;
					double x = Math.cos(lng);
					double y = Math.sin(lng);
	
					glNormal3f((float) (x * zr0), (float) (y * zr0), (float) z0);
					glVertex3f((float) (r * x * zr0), (float) (r * y * zr0), (float) (r * z0));
					glNormal3f((float) (x * zr1), (float) (y * zr1), (float) (z1));
					glVertex3f((float) (r * x * zr1), (float) (r * y * zr1), (float) (r * z1));
				}
			glEnd();
		}
	}

	// L�sung von Dustin Heyer
	public static void renderPyramide2() {  
		glBegin(GL_TRIANGLES);
			// boden
			glVertex3f(-1, -1, -1);
			glVertex3f(-1, +1, -1);
			glVertex3f(+1, -1, -1);
			glVertex3f(+1, +1, -1);
			glVertex3f(-1, +1, -1);
			glVertex3f(+1, -1, -1);
	
			// vier seiten
			// S1
			glVertex3f(+1, +1, -1);
			glVertex3f(-1, +1, -1);
			glVertex3f(0, 0, 2);
	
			// s2
			glVertex3f(+1, +1, -1);
			glVertex3f(+1, -1, -1);
			glVertex3f(0, 0, 2);
	
			// S3
			glVertex3f(-1, +1, -1);
			glVertex3f(-1, -1, -1);
			glVertex3f(0, 0, 2);
			
			// S4
			glVertex3f(-1, -1, -1);
			glVertex3f(-1, +1, -1);
			glVertex3f(0, 0, 2);
		glEnd();
	}

	// L�sung von Dustin Heyer
	public static void renderStern2(int n) { 
		double winkel = 360 / n;
		double halbwinkel=winkel/2;
		double radIn=0.5;
		double radOut=1.0;
		
		glBegin(GL_TRIANGLES);
			for(int i = 0; i < n;i++) {			
				// innen
				glVertex3f((float)(radIn*Math.cos(Math.toRadians(winkel*i))), (float)(radIn*Math.sin(Math.toRadians(winkel*(i)))), 0);
				glVertex3f((float)(radIn*Math.cos(Math.toRadians(winkel*(i+1)))), (float)(radIn*Math.sin(Math.toRadians(winkel*(i+1)))), 0);
				glVertex3f(0, 0, 0);
				// au�en
				glVertex3f((float)(radIn*Math.cos(Math.toRadians(winkel*i))), (float)(radIn*Math.sin(Math.toRadians(winkel*(i)))), 0);
				glVertex3f((float)(radIn*Math.cos(Math.toRadians(winkel*(i+1)))), (float)(radIn*Math.sin(Math.toRadians(winkel*(i+1)))), 0);
				glVertex3f((float)(radOut*Math.cos(Math.toRadians(winkel*(i)+halbwinkel))), (float)(radOut*Math.sin(Math.toRadians(winkel*(i)+halbwinkel))), 0);
			}
		glEnd();
	}
}
