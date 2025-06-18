import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;

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

        public void update(List<Boid> swarm) {
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

    public static void main(String[] args) {
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

    private static void runMainLoop() {
        while (!Display.isCloseRequested()) {
            time += 0.016f;

            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                useObjModel = !useObjModel && (zombieModel != null);
                while (Keyboard.isKeyDown(Keyboard.KEY_Z));
            }

            updateSwarm();
            renderFrame();
            
            Display.update();
            Display.sync(60);
        }
    }

    private static void updateSwarm() {
        for (Boid boid : swarm) {
            boid.update(swarm);
            keepInBounds(boid);
        }
    }

    private static void keepInBounds(Boid boid) {
        float turnForce = 0.1f;
        if (boid.position.x < -WORLD_BOUNDS) boid.velocity.x += turnForce;
        if (boid.position.x > WORLD_BOUNDS) boid.velocity.x -= turnForce;
        if (boid.position.z < -WORLD_BOUNDS) boid.velocity.z += turnForce;
        if (boid.position.z > WORLD_BOUNDS) boid.velocity.z -= turnForce;
        boid.position.y = 0; // Auf Boden halten
    }

    private static void renderFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Kamera
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(60, (float)Display.getWidth()/Display.getHeight(), 0.1f, 300);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        GLU.gluLookAt(0, 40, 50, 0, 0, 0, 0, 1, 0);

        // Boden
        drawGround();

        // Zombies
        for (Boid boid : swarm) {
            glPushMatrix();
            glTranslatef(boid.position.x, boid.position.y, boid.position.z);
            
            float angle = (float)Math.toDegrees(Math.atan2(boid.velocity.x, boid.velocity.z));
            glRotatef(angle, 0, 1, 0);

            if (useObjModel && zombieModel != null) {
                glScalef(0.5f, 0.5f, 0.5f);
                glColor3f(0.3f, 0.7f, 0.3f);
                POGL.renderObject(zombieModel);
            } else {
                renderPrimitiveZombie();
            }
            glPopMatrix();
        }
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