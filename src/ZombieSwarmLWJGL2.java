import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ZombieSwarmLWJGL2 {
    // 1. Vector3f Klasse (erweitert)
    public static class Vector3f {
        public float x, y, z;
        
        public Vector3f() { this(0, 0, 0); }
        
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
        
        public void normalize() {
            float len = length();
            if (len != 0) {
                x /= len;
                y /= len;
                z /= len;
            }
        }
    }

    // 2. Boid Klasse (angepasst)
    public static class Boid {
        public Vector3f position = new Vector3f();
        public Vector3f velocity = new Vector3f();
        private static final float MAX_SPEED = 0.5f;
        private static final float NEIGHBOR_RADIUS = 8.0f;

        public void update(List<Boid> swarm, float currentTime) { // Zeit als Parameter
            Vector3f alignment = new Vector3f();
            Vector3f cohesion = new Vector3f();
            Vector3f separation = new Vector3f();
            int neighborCount = 0;

            for (Boid other : swarm) {
                if (other != this) {
                    float dist = position.distance(other.position);
                    if (dist < NEIGHBOR_RADIUS) {
                        // Alignment
                        alignment.add(other.velocity);
                        
                        // Cohesion
                        cohesion.add(other.position);
                        
                        // Separation
                        Vector3f diff = new Vector3f(
                            position.x - other.position.x,
                            position.y - other.position.y,
                            position.z - other.position.z
                        );
                        diff.scale(1f / (dist * dist + 0.0001f));
                        separation.add(diff);
                        
                        neighborCount++;
                    }
                }
            }

            if (neighborCount > 0) {
                // Alignment anwenden
                alignment.scale(1f / neighborCount);
                alignment.scale(0.5f);
                velocity.add(alignment);

                // Cohesion anwenden
                cohesion.scale(1f / neighborCount);
                cohesion.subtract(position);
                cohesion.scale(0.05f);
                velocity.add(cohesion);

                // Separation anwenden
                separation.scale(0.05f);
                velocity.add(separation);
            }

            // Geschwindigkeit begrenzen
            float speed = velocity.length();
            if (speed > MAX_SPEED) {
                velocity.scale(MAX_SPEED / speed);
            }

            // Position aktualisieren
            position.add(velocity);
        }
    }

    // 3. Hauptprogramm
    private static List<Boid> swarm = new ArrayList<>();
    private static float time = 0;
    private static boolean useObjModel = false;
    private static Model zombieModel;
    private static final float WORLD_BOUNDS = 30f;

    // Shader-Variablen
    private static int zombieShaderProgram;
    private static int timeUniform;
    
    // Zombie Vertex Shader
    private static String zombieVertexShader = ""
        + "#version 130\n"
        + "uniform float time;\n"
        + "void main() {"
        + "   vec4 pos = gl_Vertex;"
        + "   pos.y += sin(time * 3.0 + pos.x * 2.0) * 0.1;"  // Wackel-Effekt
        + "   gl_Position = gl_ModelViewProjectionMatrix * pos;" 
        + "}";

    // Zombie Fragment Shader - animierte grüne Zombie-Haut
    private static String zombieFragmentShader = ""
        + "#version 130\n"
        + "uniform float time;\n"
        + "void main() {" 
        + "   float pulse = sin(time * 2.0) * 0.5 + 0.5;"
        + "   vec3 zombieColor = vec3(0.2 + pulse * 0.3, 0.6 + pulse * 0.2, 0.2);"
        + "   gl_FragColor = vec4(zombieColor, 1.0);" 
        + "}";

    // Boden Shader - Schachbrettmuster
    private static String groundFragmentShader = ""
        + "#version 130\n"
        + "void main() {"
        + "   vec2 pos = gl_FragCoord.xy * 0.1;"
        + "   float pattern = mod(floor(pos.x) + floor(pos.y), 2.0);"
        + "   vec3 color = mix(vec3(0.2, 0.15, 0.1), vec3(0.4, 0.35, 0.3), pattern);"
        + "   gl_FragColor = vec4(color, 1.0);"
        + "}";

    public static void main(String[] args) {
        System.out.println("Willkommen zur Zombie Simulation!");
        System.out.println("Z drücken um das Modell umzuschalten");
        try {
            initialize();
            runMainLoop();
        } catch (LWJGLException e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void initialize() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(1024, 768));
        Display.create();
        Display.setTitle("Zombie Schwarm Simulation");
        Keyboard.create();

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.15f, 0.2f, 1.0f);
        
        // Shader initialisieren
        prepareZombieShader();

        try {
            zombieModel = POGL.loadModel(new File("assets/Zombie2.obj"));
            zombieModel.size = 0.1f;
        } catch (Exception e) {
            System.err.println("OBJ-Modell nicht geladen");
        }

        // Schwarm erstellen
        for (int i = 0; i < 10; i++) {
            Boid boid = new Boid();
            boid.position.set(
                (float)(Math.random() * 10 - 5),
                0,
                (float)(Math.random() * 10 - 5)
            );
            boid.velocity.set(
                (float)(Math.random() * 0.2 - 0.1),
                0,
                (float)(Math.random() * 0.1)
            );
            swarm.add(boid);
        }
    }
    
    private static void prepareZombieShader() {
        zombieShaderProgram = glCreateProgram();

        // Vertex Shader
        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertShader, zombieVertexShader);
        glCompileShader(vertShader);
        String vertLog = glGetShaderInfoLog(vertShader, 1024);
        if (!vertLog.isEmpty()) {
            System.out.println("Vertex Shader Log: " + vertLog);
        }
        glAttachShader(zombieShaderProgram, vertShader);

        // Fragment Shader
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragShader, zombieFragmentShader);
        glCompileShader(fragShader);
        String fragLog = glGetShaderInfoLog(fragShader, 1024);
        if (!fragLog.isEmpty()) {
            System.out.println("Fragment Shader Log: " + fragLog);
        }
        glAttachShader(zombieShaderProgram, fragShader);

        glLinkProgram(zombieShaderProgram);
        
        // Program Link Status prüfen
        String programLog = glGetProgramInfoLog(zombieShaderProgram, 1024);
        if (!programLog.isEmpty()) {
            System.out.println("Program Link Log: " + programLog);
        }
        
        // Uniform-Location für Zeit
        timeUniform = glGetUniformLocation(zombieShaderProgram, "time");
    }

    private static void checkShaderCompile(int shader) {
        IntBuffer compiled = BufferUtils.createIntBuffer(1);
        glGetShader(shader, GL_COMPILE_STATUS, compiled);
        if (compiled.get(0) == GL_FALSE) {
            System.err.println("Shader Kompilierungsfehler:");
            System.err.println(glGetShaderInfoLog(shader, 1024));
        }
    }

    private static boolean zKeyPressed = false;

    private static void runMainLoop() {
        while (!Display.isCloseRequested()) {
            time += 0.016f;

            // KORRIGIERTER Code für Z-Taste
            boolean currentZKeyState = Keyboard.isKeyDown(Keyboard.KEY_Z);
            
            if (currentZKeyState && !zKeyPressed) {
                // Taste wurde gerade gedrückt (Flanke)
                useObjModel = !useObjModel && (zombieModel != null);
                System.out.println("Zombie-Modus umgeschaltet: " + 
                    (useObjModel ? "OBJ-Modell" : "Primitive Würfel"));
            }
            
            zKeyPressed = currentZKeyState; // Zustand speichern

            updateSwarm();
            renderFrame();
            
            Display.update();
            Display.sync(60);
        }
    }

    private static void updateSwarm() {
        for (Boid boid : swarm) {
            boid.update(swarm, time); // Zeit als Parameter übergeben
            keepInBounds(boid);
        }
    }

    private static void keepInBounds(Boid boid) {
        float turnForce = 0.1f;
        if (boid.position.x < -WORLD_BOUNDS+2) boid.velocity.x += turnForce;
        if (boid.position.x > WORLD_BOUNDS+2) boid.velocity.x -= turnForce;
        if (boid.position.z < -WORLD_BOUNDS+2) boid.velocity.z += turnForce;
        if (boid.position.z > WORLD_BOUNDS+2) boid.velocity.z -= turnForce;
        boid.position.y = 0.5f; // Zombies stehen AUF dem Boden (halbe Höhe)
    }

    private static void renderFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Kamera
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(60, (float)Display.getWidth()/Display.getHeight(), 0.1f, 300);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        //Kamera-Position ändern
        GLU.gluLookAt(
            0, 25, 50,    // Kamera-Position (x, y, z)
            0, 0, 0,      // Blickrichtung/Ziel (x, y, z)
            0, 1, 0       // Up-Vektor (x, y, z)
        );
        
        // Boden (ohne Shader)
        glUseProgram(0); // Zurück zur Fixed Function Pipeline
        drawGround();

        // Zombies mit Shader
        glUseProgram(zombieShaderProgram);
        glUniform1f(timeUniform, time); // Zeit für Animation

        for (Boid boid : swarm) {
            glPushMatrix();
            glTranslatef(boid.position.x, boid.position.y + 0.5f, boid.position.z);
            
            float angle = (float)Math.toDegrees(Math.atan2(boid.velocity.x, boid.velocity.z));
            glRotatef(angle, 0, 1, 0);

            if (useObjModel && zombieModel != null) {
                // KORREKTUR: Zombie-Modell um 90 Grad drehen, damit es nach vorne schaut
                glRotatef(90, 0, 1, 0); // Zusätzliche Rotation um Y-Achse
                glScalef(0.5f, 0.5f, 0.5f);
                POGL.renderObject(zombieModel);
            } else {
                renderPrimitiveZombie();
            }
            glPopMatrix();
        }
        
        glUseProgram(0); // Shader deaktivieren
    }

    private static void drawGround() {
        glBegin(GL_QUADS);
        glColor3f(0.3f, 0.25f, 0.2f);
        glVertex3f(-WORLD_BOUNDS, 0, -WORLD_BOUNDS);
        glVertex3f(-WORLD_BOUNDS, 0, WORLD_BOUNDS);
        glVertex3f(WORLD_BOUNDS, 0, WORLD_BOUNDS);
        glVertex3f(WORLD_BOUNDS, 0, -WORLD_BOUNDS);
        glEnd();
    }

    private static void renderPrimitiveZombie() {
        // Körper
        glColor3f(0.3f, 0.7f, 0.3f);
        renderCube(0, 0.4f, 0, 0.4f, 0.8f, 0.2f);

        // Kopf
        glColor3f(0.2f, 0.5f, 0.2f);
        renderCube(0, 0.9f, 0, 0.3f, 0.3f, 0.3f);

        // Arme
        glColor3f(0.4f, 0.6f, 0.3f);
        renderCube(-0.4f, 0.5f, 0, 0.15f, 0.6f, 0.15f);
        renderCube(0.4f, 0.5f, 0, 0.15f, 0.6f, 0.15f);
    }

    private static void renderCube(float x, float y, float z, float w, float h, float d) {
        float hw = w/2, hh = h/2, hd = d/2;

        glPushMatrix();
        glTranslatef(x, y, z);

        glBegin(GL_QUADS);
            // Vorderseite
            glVertex3f(-hw, -hh, hd);
            glVertex3f(hw, -hh, hd);
            glVertex3f(hw, hh, hd);
            glVertex3f(-hw, hh, hd);
            // Rückseite
            glVertex3f(-hw, -hh, -hd);
            glVertex3f(-hw, hh, -hd);
            glVertex3f(hw, hh, -hd);
            glVertex3f(hw, -hh, -hd);
            // Links
            glVertex3f(-hw, -hh, -hd);
            glVertex3f(-hw, -hh, hd);
            glVertex3f(-hw, hh, hd);
            glVertex3f(-hw, hh, -hd);
            // Rechts
            glVertex3f(hw, -hh, hd);
            glVertex3f(hw, -hh, -hd);
            glVertex3f(hw, hh, -hd);
            glVertex3f(hw, hh, hd);
            // Oben
            glVertex3f(-hw, hh, hd);
            glVertex3f(hw, hh, hd);
            glVertex3f(hw, hh, -hd);
            glVertex3f(-hw, hh, -hd);
            // Unten
            glVertex3f(-hw, -hh, hd);
            glVertex3f(-hw, -hh, -hd);
            glVertex3f(hw, -hh, -hd);
            glVertex3f(hw, -hh, hd);
        glEnd();

        glPopMatrix();
    }

    private static void cleanup() {
        Keyboard.destroy();
        Display.destroy();
    }
}