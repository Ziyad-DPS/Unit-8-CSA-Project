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

    private boolean keyHeldDown;
    
    public Game(GuiHandler guiHandler) {
        // https://www.geeksforgeeks.org/java/java-jlayeredpane/ link to learn about JLayeredPane
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        
        this.guiHandler = guiHandler;
        this.player = new Player(); 
        this.rocket = new Rocket(player);
        this.ball = new Ball();

        this.keyHeldDown = false;

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
        
        Toolkit.getDefaultToolkit().sync();
        
        g.setColor(Color.RED);
        ball.render(g);
        rocket.render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyHeldDown) { return; }

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

        public void render(Graphics g) {
            g.fillOval(x, y, 30, 30);
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
