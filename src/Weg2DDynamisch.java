

import java.util.ArrayList;

public class Weg2DDynamisch {
	private ArrayList<Vektor2D> liste;
	private int max;
	
	public Weg2DDynamisch(int max) {
		liste = new ArrayList<Vektor2D>();
		this.max = max;
	}
	
	public void addWaypoint(Vektor2D element) {
		liste.add(0, element);
		if (liste.size()>max)
			liste.remove(liste.size()-1);
	}
	
	public int getSize() {
		return liste.size();
	}
	
	public Vektor2D getElement(int index) {
		return liste.get(index);
	}
	
	public Vektor2D[] getElementList() {
		Vektor2D[] liste = new Vektor2D[this.liste.size()];
		
		for (int i=0; i<this.liste.size(); i++)
			liste[i] = this.getElement(i);
		
		return liste;
	}
}
