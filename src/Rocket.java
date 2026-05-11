import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class Rocket {
    
    private int time;
    private double x, y, yVelocity, acceleration, fuelCapacity, power, max_fuel, durability, maxDurability, lastDistance, scaleX, scaleY;
    private final double DISTANCE_TO_MOON, TERMINAL_VELOCITY, FUEL_CONSUMPTION, DURABILITY_CONSUMPTION, GRAVITY, GROUND;
    private final int IMAGE_WIDTH, PANEL_WIDTH, PANEL_HEIGHT;
    private BufferedImage image;
    private boolean setUp, runComplete, timer, gameWon;
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
        this.durability = 0;
        this.maxDurability = 0;
        this.time = 0;
        this.timer = false;
        this.setUp = false;
        this.runComplete = false;
        this.gameWon = false;
        this.player = player;

        // Find the rocket image and deal with IOerror handling
        try {
            File file = new File("src/resources/rocketship.png");
            this.image = ImageIO.read(file);
        } catch (IOException error) {
            System.err.println(error);
        }
        
        // Get a Dimension named screenSize
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Get the screen sizes
        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        
        // Used to resize things for different screen sizes
        this.scaleX = (double) PANEL_WIDTH / 2560;
        this.scaleY = (double) PANEL_HEIGHT / 1600;
        
        // Create constants
        this.DISTANCE_TO_MOON = -200000;
        this.TERMINAL_VELOCITY = 40;
        this.GRAVITY = 1.24;
        this.FUEL_CONSUMPTION = 0.2;
        this.DURABILITY_CONSUMPTION = 0.08;
        this.IMAGE_WIDTH = (int) (400 * scaleX);
        this.GROUND = (int) PANEL_HEIGHT / 2 + (int) (image.getHeight() * scaleY);
    }
    
    // Updates variables for physics e.g yVelocity, collisionDetection etc
    public void update() {
        /* 
            Creates a timer based on the screens FPS
            to restart the game when the rocket starts falling
        */
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

        // Update the yPosition
        y += yVelocity;
        
        // Collision detection, is rocket falling, and update yVelocity
        borderCheck();
        rocketFalling();
        updateDurability();
        updateVelocity();
    }

    // Update yVelocity based off of fuelCapacity and use the easing function
    public void updateVelocity() {
        // Base conditions 
        if (fuelCapacity < 0 || !setUp || gameWon) { return; }

        /* 
            Acceleration gets a x value 
            and uses it to smooth the acceleration of the rocket
        */
        acceleration = easingFunction(1 - (fuelCapacity / max_fuel)) * power;

        // Apply acceleration and get rid of some fuel
        yVelocity -= acceleration;
        fuelCapacity -= FUEL_CONSUMPTION;
    }

    private void updateDurability() {
    if (!setUp || runComplete || gameWon) { return; }

    if (y < GROUND) {
        durability -= DURABILITY_CONSUMPTION;
    }

    if (durability <= 0) {
        durability = 0;
        runComplete = true;
        timer = true;
    }
}
    
    // renders the rocket image
    public void render(Graphics g) {
        // Place the image in the center and scale it to fit the users screen size
        g.drawImage(
            image,
            (int) PANEL_WIDTH / 2 - (IMAGE_WIDTH / 2),
            (int) PANEL_HEIGHT - IMAGE_WIDTH - (int) (85 * scaleY),
            IMAGE_WIDTH,
            IMAGE_WIDTH,
            null
        );
    }

    // Checks if the rocket is falling
    private void rocketFalling() {
        // Base conditions
        if (!setUp || runComplete || gameWon) { return; }
        
        // If the rocket is falling then set the timer up
        // add spacebucks
        // and say the run/flight was complete
        if (getDistance() - lastDistance <= -300) {
            player.distanceToSpaceBucks((int) lastDistance);
            runComplete = true;
            timer = true;
            return;
        }
        
        // sets lastDistance to current distance if the rocket isnt falling
        if (lastDistance < getDistance()) {
            lastDistance = getDistance();
        }
    }
    
    // Physics with gravity and ground collision
    private void borderCheck() {
        // If the rocket is on the ground
        // or below it set y to the ground
        if (y >= GROUND) {
            y = GROUND;
        } 
        // if the rocket is not at its terminal velocity add gravity
        else if (yVelocity < TERMINAL_VELOCITY) {
            yVelocity += GRAVITY;
        }

        else if (y <= DISTANCE_TO_MOON + (int) (150 * scaleY)) {
            gameWon = true;
        }
    }

    // An easing function derived from https://easings.net/
    private double easingFunction(double x) {
        return 0.15 * x * x;
    }

    // Set ups the launch for the rocket setting fuel capaicty and power
    public void setUpLaunch() {
        if (runComplete || setUp || y != GROUND) { return; }
        max_fuel = 95 + player.getFuelLevel() * 1.25;
        fuelCapacity = max_fuel;

        power = 16 + player.getPowerLevel() * 0.2;

        maxDurability = 100 + player.getDurabilityLevel() * 15;
        durability = maxDurability;

        lastDistance = 0;
        setUp = true;
        timer = false;
    }

    // Gets the distance for the rocket from the ground
    public int getDistance() {
        if (y > GROUND) { return 0; }
        return Math.abs((int) y - (int) GROUND);
    }

    public boolean getGameWon() {
        return gameWon;
    }

    // Getter for x
    public int getX() {
        return (int) x;
    }

    // Getter for y
    public int getY() {
        return (int) y;
    }

    public double getFuel() {
        return fuelCapacity;
    }

    public double getMaxFuel() {
        return max_fuel;
    }

    public double getDurability() {
        return durability;
    }

    public double getMaxDurability() {
        return maxDurability;
    }

}
