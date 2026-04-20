import java.awt.*;
import java.lang.*;
import javax.swing.*;

public class Game extends JPanel {
    
    /*
    Game handles rendering pictures (rocket, background, etc) and gamestate
    */

    // Custom graphics website using JFrame https://www3.ntu.edu.sg/home/ehchua/programming/java/J4b_CustomGraphics.html
    private GuiHandler guiHandler;
    private Player player;
    private Rocket rocket;
    private int PANEL_WIDTH, PANEL_HEIGHT;
    private Ball ball;

    public Game(GuiHandler guiHandler) {
        super(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.guiHandler = guiHandler;
        this.player = new Player(); 
        this.rocket = new Rocket();
        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        this.ball = new Ball();
        setPreferredSize(screenSize);
    }

    public void initGame() throws InterruptedException {
        boolean running = true;
        while (running) {
            ball.moveBall();
            repaint();
            Thread.sleep(16);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Toolkit.getDefaultToolkit().sync();

        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        
        g.setColor(Color.RED);
        ball.render(g);

    }

    private class Ball {
        
        private int x, y, xV, yV;

        public Ball() {
            this.x = 260;
            this.y = 730;
            this.xV = 6;
            this.yV = 9;
        }

        public void moveBall() {
            borderCheck();

            x += xV;
            y += yV;
        }

        public void render(Graphics g) {
            g.fillOval(x, y, 30, 30);
        }

        private void borderCheck() {
            if (x + 30 < 0 || x - 30 > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
                xV = -xV;
            }

            if (y + 30 < 0 || y - 30 > Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
                yV = -yV;
            }
        }

    }
}
