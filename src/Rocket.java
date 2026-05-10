import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Rocket {
    
    private int time;
    private double x, y, yVelocity, acceleration, fuelCapacity, power, max_fuel, lastDistance, scaleX, scaleY;
    private final double DISTANCE_TO_MOON, TERMINAL_VELOCITY, FUEL_CONSUMPTION, GRAVITY, GROUND;
    private final int IMAGE_WIDTH, IMAGE_X, IMAGE_Y, PANEL_WIDTH, PANEL_HEIGHT, MAX_PARTICLES;
    private ArrayList<Particle> particles;
    private BufferedImage image;
    private boolean setUp, runComplete, timer;
    private Player player;

    public Rocket(Player player) {
        this.x = 0;
        this.y = 0;
        this.yVelocity = 0;
        this.lastDistance = 0;
        this.acceleration = 0;
        this.fuelCapacity = 0;
        this.max_fuel = 0;
        this.power = 0;
        this.time = 0;
        this.timer = false;
        this.setUp = false;
        this.runComplete = false;
        this.player = player;

        this.particles = new ArrayList<Particle>();

        try {
            File file = new File("src/resources/rocketship.png");
            this.image = ImageIO.read(file);
        } catch (IOException error) {
            System.err.println(error);
        }

        // Constants
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();

        this.scaleX = (double) PANEL_WIDTH / 2560;
        this.scaleY = (double) PANEL_HEIGHT / 1600;
        
        this.DISTANCE_TO_MOON = 20000;
        this.TERMINAL_VELOCITY = 40;
        this.GRAVITY = 1.24;
        this.FUEL_CONSUMPTION = 0.2;
        this.IMAGE_WIDTH = (int) (400 * scaleX);
        this.IMAGE_X = (int) PANEL_WIDTH / 2 - (IMAGE_WIDTH / 2);
        this.IMAGE_Y = (int) PANEL_HEIGHT - IMAGE_WIDTH - (int) (85 * scaleY);
        this.GROUND = (int) PANEL_HEIGHT / 2 + (int) (image.getHeight() * scaleY);
        this.MAX_PARTICLES = 400;
    }
    
    public void update() {
        if (timer) {
            if (time < 60) {
                time++;
            } 
            else {
                timer = false;
                runComplete = false;
                setUp = false;
                time = 0;
                y = 0;
                yVelocity = 0;
                fuelCapacity = 0;
            }
        }
        
        y += yVelocity;
        particles = filterParticles();
        borderCheck();
        rocketFalling();
        updateVelocity();
        updateParticles();
    }
    
    public void updateVelocity() {
        if (fuelCapacity < 0 || !setUp) { return; }
        
        acceleration = easingFunction(1 - (fuelCapacity / max_fuel)) * power;
        yVelocity -= acceleration;
        fuelCapacity -= FUEL_CONSUMPTION;
    }
    
    public void render(Graphics g) {
        if (particles.size() < MAX_PARTICLES && !(!setUp || runComplete)) {
            particles = addParticles();
        }

        for (Particle particle : particles) {
            particle.render(g);
        }

        g.drawImage(
            image,
            IMAGE_X,
            IMAGE_Y,
            IMAGE_WIDTH,
            IMAGE_WIDTH,
            null
        );
    }

    private void rocketFalling() {
        if (!setUp || runComplete) { return; }
        
        if (getDistance() - lastDistance <= -300) {
            player.distanceToSpaceBucks((int) lastDistance);
            runComplete = true;
            timer = true;
            return;
        }
        
        if (lastDistance < getDistance()) {
            lastDistance = getDistance();
        }
    }
    
    private void borderCheck() {
        if (y >= GROUND) {
            y = GROUND;
        } 
        else if (yVelocity < TERMINAL_VELOCITY) {
            yVelocity += GRAVITY;
        }
    }

    private double easingFunction(double x) {
        return 0.15 * x * x;
    }

    public void setUpLaunch() {
        if (runComplete || setUp || y != GROUND) { return; }
        max_fuel = 95 + player.getFuelLevel() * 1.25;
        fuelCapacity = max_fuel;

        power = 16 + player.getPowerLevel() * 0.4;

        lastDistance = 0;
        setUp = true;
        timer = false;
    }

    public ArrayList<Particle> addParticles() {
        ArrayList<Particle> newParticles = particles;

        for (int i = newParticles.size(); i < MAX_PARTICLES; i++) {
            Particle particle = new Particle(
                IMAGE_X + (int) (200 * scaleX), 
                IMAGE_Y + (int) (150 * scaleY), 
                randomXVelocity(),
                randomYVelocity()
            );
            
            newParticles.add(particle);
        }

        return newParticles;
    }

    public int randomXVelocity() {
        int MIN = 1;
        int MAX = 4;
        int returnValue = (int) (Math.random() * (MAX - MIN)) + MIN;

        if (Math.random() < 0.5) {
            return -returnValue;
        }

        return returnValue;
    }

    public int randomYVelocity() {
        int MIN = 8;
        int MAX = 15;
        return (int) (Math.random() * (MAX - MIN)) + MIN;
    }

    public void updateParticles() {
        for (Particle particle : particles) {
            particle.update();
        }
    }

    public ArrayList<Particle> filterParticles() {
        ArrayList<Particle> aliveParticles = new ArrayList<Particle>();

        for (Particle particle : particles) {
            if (particle.getIsAlive()) {
                aliveParticles.add(particle);
            }
        }

        return aliveParticles;
    }

    public int getDistance() {
        if (y > GROUND) { return 0; }
        return Math.abs((int) y - (int) GROUND);
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    private class Particle {

        private int opacity, offset;
        private double x, y, xVelocity, yVelocity;
        private boolean isAlive, touchedGround;
        private final int size;
        private final int TOP, BOTTOM, GROUND;

        public Particle(double x, double y, double xVelocity, double yVelocity) {
            this.x = x;
            this.y = y;
            this.xVelocity = xVelocity;
            this.yVelocity = yVelocity;
            this.size = (int) (randomSize() * scaleX);
            this.offset = getDistance();
            this.opacity = 255;
            this.isAlive = true;
            this.touchedGround = false;

            this.TOP = (int) (-400 * scaleY);
            this.BOTTOM = (int) (PANEL_HEIGHT + 400 * scaleY);
            this.GROUND = offset + PANEL_HEIGHT - (int) (150 * scaleY);
        }

        public void render(Graphics g) {
            g.setColor(new Color(255, 255, 255, opacity));
            if (touchedGround) {
                g.fillOval(
                    (int) x,
                    (int) (y + offset),
                    size,
                    size
                );
            } 
            else {
                g.fillOval(
                    (int) x,
                    (int) y,
                    size,
                    size
                );
            }
        }

        public void update() {
            x += xVelocity;
            y += yVelocity;
            offset = getDistance();
            updateOpacity();
            collisionDetection();
        }

        public void collisionDetection() {
            if (y >= GROUND) {
                yVelocity = yVelocity * -0.5;
                xVelocity = xVelocity * 0.5;
                touchedGround = true;
            }

            if (y > BOTTOM || y < TOP) { 
                isAlive = false;
            }

            if (x < 0 || x > PANEL_WIDTH) {
                isAlive = false;
            }

        }

        private void updateOpacity() {
            if (opacity <= 1) { opacity = 1; }
            opacity -= 1;
        }

        private int randomSize() {
            int MAX = 30;
            int MIN = 15;
            return (int) (Math.random() * (MAX - MIN)) + MIN;
        }

        public boolean getIsAlive() {
            return isAlive;
        }

    }

}
