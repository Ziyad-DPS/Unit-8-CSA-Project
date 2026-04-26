import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import javax.swing.*;

public class Game extends JLayeredPane implements KeyListener {
    
    /*
    Game handles rendering pictures (rocket, background, etc) and gamestate
    */

    // Custom graphics website using JFrame https://www3.ntu.edu.sg/home/ehchua/programming/java/J4b_CustomGraphics.html
    private int PANEL_WIDTH, PANEL_HEIGHT;
    
    private GuiHandler guiHandler;
    private Player player;
    private Rocket rocket;
    private Ball ball;

    private boolean keyHeldDown, started;
    
    public Game(GuiHandler guiHandler) {
        // https://www.geeksforgeeks.org/java/java-jlayeredpane/ link to learn about JLayeredPane
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        
        this.guiHandler = guiHandler;
        this.player = new Player(); 
        this.rocket = new Rocket(player);
        this.ball = new Ball();

        guiHandler.setRocket(rocket);

        this.keyHeldDown = false;
        this.started = false;

        addKeyListener(this);

        guiHandler.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        add(guiHandler, JLayeredPane.DEFAULT_LAYER);
        setPreferredSize(screenSize);
    }

    public void initGame() throws InterruptedException {
        boolean isPaused = false;
        boolean running = true;
        while (running) {
            if (isPaused == false) {
                ball.moveBall();
                rocket.update();
            }

            isPaused = guiHandler.handlePause();
            setFocusable(true);
            repaint();

            Thread.sleep(16);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.sync();

        int offsetY = rocket.getY() - (int) toolkit.getScreenSize().getHeight() / 2;
        
        g.setColor(Color.RED);
        g.fillRect(0, -1300 - offsetY, (int) toolkit.getScreenSize().getWidth(), 240);
        ball.render(g, rocket.getX(), offsetY);
        rocket.render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyHeldDown || started) { return; }

        rocket.setUpVelocity();
        keyHeldDown = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyHeldDown = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
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

        public void render(Graphics g, int rocketX, int rocketY) {
            g.fillOval(x - rocketX, y - rocketY, 30, 30);
        }

        private void borderCheck() {
            if (x + 30 < 250 || x - 30 > Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 250) {
                xV = -xV;
            }

            if (y + 30 < 250 || y - 30 > Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 250) {
                yV = -yV;
            }
        }

    }
}
