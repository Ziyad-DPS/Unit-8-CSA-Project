import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.lang.*;

import javax.imageio.ImageIO;

public class Rocket {
    
    private int x, y, xVelocity, yVelocity;

    public Rocket() {
        this.x = 0;
        this.y = 0;
        this.xVelocity = 0;
        this.yVelocity = 0;
    }

    public void update() {
        x += xVelocity;
        y += yVelocity;
    }

    public void render(Graphics g) {
        g.drawRect(0, 0, 50, 100);
    }

}
