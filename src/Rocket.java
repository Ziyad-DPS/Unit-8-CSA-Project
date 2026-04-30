import java.awt.*;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class Rocket {
    
    private double x, y, xVelocity, yVelocity, acceleration, fuelCapacity, power, max_fuel, lastYVelocity, lastDistance;
    private final double DISTANCE_TO_MOON, TERMINAL_VELOCITY, GRAVITY, FUEL_CONSUMPTION, GROUND;
    private final int IMAGE_WIDTH;
    private BufferedImage image;
    private boolean setUp;
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
        this.power = 17.5;
        this.setUp = false;
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
        this.GROUND = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 + 130;
        this.IMAGE_WIDTH = 500;
    }
    
    public void update() {
        x += xVelocity;
        y += yVelocity;
        borderCheck();
        updateVelocity();
    }

    public void updateVelocity() {
        if (fuelCapacity < 0 || !setUp) { return; }

        lastYVelocity = yVelocity;
        acceleration = easingFunction(1 - (fuelCapacity / max_fuel)) * power;
        yVelocity -= acceleration;
        fuelCapacity -= FUEL_CONSUMPTION;
    }
    
    public void render(Graphics g, boolean gameStarted) {
        if (!gameStarted) {
            return;
        }
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        g.drawImage(
            image,
            (int) screenSize.getWidth() / 2 - (IMAGE_WIDTH / 2),
            (int) screenSize.getHeight() - IMAGE_WIDTH - 150,
            IMAGE_WIDTH,
            IMAGE_WIDTH,
            null
        );
    }

    private boolean rocketFalling() {
        if (yVelocity - lastYVelocity >= TERMINAL_VELOCITY + 95 && setUp) {
            player.distanceToSpaceBucks((int) lastDistance);
            return true;
        }
        
        if (lastDistance < getDistance() && setUp) {
            lastDistance = getDistance();
        }

        return false;
    }
    
    private void borderCheck() {
        if (y - 130 >= Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) {
            y = GROUND;
        } 
        else if (yVelocity < TERMINAL_VELOCITY) {
            yVelocity += GRAVITY;
        }
    }

    private double easingFunction(double x) {
        return 0.15 * x * x;
    }

    public void setUpVelocity() {
        int[] fuelCapacityValues = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        fuelCapacity = fuelCapacityValues[player.getFuelLevel() - 1];
        max_fuel = fuelCapacityValues[player.getFuelLevel() - 1];
        setUp = true;
    }

    public int getDistance() {
        return Math.abs((int) y - (int) GROUND);
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

}
