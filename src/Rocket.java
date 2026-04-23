import java.awt.*;

public class Rocket {
    
    private double x, y, xVelocity, yVelocity, feulCapacity;
    private Player player;

    public Rocket(Player player) {
        this.x = 0;
        this.y = 1000;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.feulCapacity = 0;
        this.player = player;   
    }

    public void update() {
        x += xVelocity;
        y += yVelocity;
        borderCheck();
        updateVelocity();
    }

    public void updateVelocity() {
        if (feulCapacity < 0) { return; }

        yVelocity -= feulCapacity / 5;
        feulCapacity -= 5;
    }
    
    public void render(Graphics g) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        g.fillRect(
            (int) screenSize.getWidth() / 2,
            (int) y,
            60,
            130
        );
    }
    
    private void borderCheck() {
        if (y - 130 >= Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) {
            y = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 + 130;
        } 
        else if (yVelocity < 40) {
            yVelocity += 1.5;
        }
    }

    public void setUpVelocity() {
        int[] feulCapacityValues = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        feulCapacity = feulCapacityValues[player.getFeulLevel() - 1];
    }

}
