import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.ArrayList;

public class Rocket {
    
    private int time;
    private double x, y, yVelocity, acceleration, fuelCapacity, power, max_fuel, lastDistance, scaleX, scaleY;
    private final double DISTANCE_TO_MOON, TERMINAL_VELOCITY, FUEL_CONSUMPTION, GRAVITY, GROUND;
    private final int IMAGE_WIDTH, PANEL_WIDTH, PANEL_HEIGHT;
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
        this.GROUND = (int) PANEL_HEIGHT / 2 + (int) (image.getHeight() * scaleY);
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
        borderCheck();
        rocketFalling();
        updateVelocity();
    }

    public void updateVelocity() {
        if (fuelCapacity < 0 || !setUp) { return; }

        acceleration = easingFunction(1 - (fuelCapacity / max_fuel)) * power;
        yVelocity -= acceleration;
        fuelCapacity -= FUEL_CONSUMPTION;
    }
    
    public void render(Graphics g) {
        g.drawImage(
            image,
            (int) PANEL_WIDTH / 2 - (IMAGE_WIDTH / 2),
            (int) PANEL_HEIGHT - IMAGE_WIDTH - (int) (85 * scaleY),
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

        private int x, y, xVelocity, yVelocity;
        private final int size;

        public Particle() {
            this.size = (int) (randomSize() * scaleX);
        }

        public void render(Graphics g) {
            g.fillOval(
                x,
                y,
                size,
                size
            );
        }

        public void update() {
            x += xVelocity;
            y += yVelocity;
        }

        public void collisionDetection() {
            
        }

        private int randomSize() {
            int MAX = 15;
            int MIN = 5;
            return (int) (Math.random() * (MAX - MIN)) + MIN;
        }

    }

}
