public class Boid {
    public Vector3f position = new Vector3f();
    public Vector3f velocity = new Vector3f();
    private static final float MAX_SPEED = 0.3f;
    private static final float NEIGHBOR_RADIUS = 5.0f;

    public void update(List<Boid> swarm) {
        List<Boid> neighbors = getNeighbors(swarm);
        
        Vector3f alignment = new Vector3f();
        Vector3f cohesion = new Vector3f();
        Vector3f separation = new Vector3f();
        
        if (!neighbors.isEmpty()) {
            // 1. Alignment - Gleiche Richtung wie Nachbarn
            for (Boid b : neighbors) {
                alignment.add(b.velocity);
            }
            alignment.scale(1f / neighbors.size());
            alignment.subtract(velocity);
            alignment.scale(0.1f);

            // 2. Cohesion - Zum Mittelpunkt der Nachbarn
            for (Boid b : neighbors) {
                cohesion.add(b.position);
            }
            cohesion.scale(1f / neighbors.size());
            cohesion.subtract(position);
            cohesion.scale(0.03f);

            // 3. Separation - Abstand halten
            for (Boid b : neighbors) {
                Vector3f diff = new Vector3f(position);
                diff.subtract(b.position);
                float dist = position.distance(b.position);
                diff.scale(1f / (dist * dist));
                separation.add(diff);
            }
            separation.scale(0.05f);
        }

        // Grundbewegung mit leichtem Schwanken
        velocity.add(new Vector3f(
            (float)Math.sin(time * 0.5f) * 0.01f,
            0,
            0.02f
        ));

        // Boid-Regeln anwenden
        velocity.add(alignment);
        velocity.add(cohesion);
        velocity.add(separation);

        // Geschwindigkeit begrenzen
        float speed = velocity.length();
        if (speed > MAX_SPEED) {
            velocity.scale(MAX_SPEED / speed);
        }

        position.add(velocity);
    }

    private List<Boid> getNeighbors(List<Boid> swarm) {
        List<Boid> neighbors = new ArrayList<>();
        for (Boid b : swarm) {
            if (b != this && position.distance(b.position) < NEIGHBOR_RADIUS) {
                neighbors.add(b);
            }
        }
        return neighbors;
    }
}