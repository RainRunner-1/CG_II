import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/*
 * Beschreibung:
 * Als Erweiterung kommt hinzu, dass wir ein Canvas-Objekt zum zeichnen
 * �bergeben k�nnen. Dazu m�ssen wir den init-Teil selbst aktiv im Konstruktor
 * ausf�hren und start() auf den renderLoop() und das Abmelden reduzieren.
 */
public abstract class LWJGLBasisFenster {
   public int WIDTH, HEIGHT;
   public String TITLE;

   public LWJGLBasisFenster() {
      this("LWJGLBasisFenster", 1500, 800);
   }

   public LWJGLBasisFenster(int width, int height) {
      this("LWJGLBasisFenster", width, height);
   }

   public LWJGLBasisFenster(String title, int width, int height) {
      WIDTH = width;
      HEIGHT = height;
      TITLE = /*"vividus Wissenschaftsverlag. Warum sich der Dino furchtbar erschreckte. " +*/ title;
   }

   public void initDisplay(Canvas c) {
      try {
         Display.setParent(c);
         Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
         Display.setTitle(TITLE);
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         Display.setLocation((d.width-WIDTH)/2, (d.height-HEIGHT)/2);
         Display.create();
      } catch (LWJGLException e) {
         e.printStackTrace();
      }
   }

   public void initDisplay() {
      try {
         Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
         Display.setTitle(TITLE);
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         Display.setLocation((d.width-WIDTH)/2, (d.height-HEIGHT)/2);
         Display.create();
      } catch (LWJGLException e) {
         e.printStackTrace();
      }
   }

   public abstract void renderLoop();

   public void start() {
      renderLoop();
      Display.destroy();
      System.exit(0);
   }
}
