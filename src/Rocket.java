import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class Rocket {
    
    private double x, y, xVelocity, yVelocity, acceleration, fuelCapacity, power, max_fuel, lastYVelocity, lastDistance;
    private final double DISTANCE_TO_MOON, TERMINAL_VELOCITY, FUEL_CONSUMPTION, GRAVITY, GROUND;
    private final int IMAGE_WIDTH;
    private BufferedImage image;
    private boolean setUp, runComplete;
    private Player player;

    public Rocket(Player player) {
        this.x = 0;
        this.y = 0;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.lastYVelocity = 0;
        this.lastDistance = 0;
        this.acceleration = 0;
        this.fuelCapacity = 0;
        this.max_fuel = 0;
        this.power = 0;
        this.setUp = false;
        this.runComplete = false;
        this.player = player;

        try {
            File file = new File("src/resources/rocketship.png");
            this.image = ImageIO.read(file);
        } catch (IOException error) {
            System.err.println(error);
        }
        
        // Constants
        this.DISTANCE_TO_MOON = 20000;
        this.TERMINAL_VELOCITY = 40;
        this.GRAVITY = 1.24;
        this.FUEL_CONSUMPTION = 0.2;
        this.GROUND = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 + image.getHeight();
        this.IMAGE_WIDTH = 400;
    }
    
    public void update() {
        x += xVelocity;
        y += yVelocity;
        borderCheck();
        updateVelocity();
        rocketFalling();
    }

    public void updateVelocity() {
        if (fuelCapacity < 0 || !setUp) { return; }

        lastYVelocity = yVelocity;
        acceleration = easingFunction(1 - (fuelCapacity / max_fuel)) * power;
        yVelocity -= acceleration;
        fuelCapacity -= FUEL_CONSUMPTION;
    }
    
    public void render(Graphics g) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        g.drawImage(
            image,
            (int) screenSize.getWidth() / 2 - (IMAGE_WIDTH / 2),
            (int) screenSize.getHeight() - IMAGE_WIDTH - 85,
            IMAGE_WIDTH,
            IMAGE_WIDTH,
            null
        );
    }

    private void rocketFalling() {
        if (!setUp || runComplete) { return; }

        if (yVelocity - lastYVelocity >= TERMINAL_VELOCITY + 50) {
            player.distanceToSpaceBucks((int) lastDistance);
            runComplete = true;
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
        max_fuel = 95 + player.getFuelLevel() * 1.25;
        fuelCapacity = max_fuel;

        power = 16 + player.getPowerLevel() * 0.25;

        lastDistance = 0;
        runComplete = false;
        setUp = true;
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

}
