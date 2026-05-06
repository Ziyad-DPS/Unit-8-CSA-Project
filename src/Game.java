import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
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
    private BufferedImage startscreenImage, backgroundImage, asteriodImage, constalationImage, meteorImage, moonImage, planetImage;
    private GameImage[] images;

    private boolean keyHeldDown, started;
    
    public Game(GuiHandler guiHandler) {
        // https://www.geeksforgeeks.org/java/java-jlayeredpane/ link to learn about JLayeredPane
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        
        this.guiHandler = guiHandler;
        this.player = new Player(); 
        this.rocket = new Rocket(player);

        guiHandler.setRocket(rocket);
        guiHandler.setPlayer(player);

        this.keyHeldDown = false;
        this.started = false;

        try {
            this.startscreenImage = ImageIO.read(new File("src/resources/start-Screen.png"));
            this.backgroundImage = ImageIO.read(new File("src/resources/play-Screen.png"));
            this.moonImage = ImageIO.read(new File("src/resources/moon.png"));
            this.asteriodImage = ImageIO.read(new File("src/resources/asteroid.png"));
            this.constalationImage = ImageIO.read(new File("src/resources/constalation.png"));
            this.meteorImage = ImageIO.read(new File("src/resources/meteor.png"));
            this.planetImage = ImageIO.read(new File("src/resources/planet.png"));
        } catch (IOException error) {
            System.err.println(error);
        }

        this.images = new GameImage[40];

        addKeyListener(this);

        guiHandler.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        add(guiHandler, JLayeredPane.DEFAULT_LAYER);
        setPreferredSize(screenSize);
    }

    public void initGame() throws InterruptedException {
        generateImages();

        boolean isPaused = false;
        boolean running = true;
        while (running) {
            if (isPaused == false) {
                rocket.update();
            }

            isPaused = guiHandler.getUiPaused();
            setFocusable(true);
            repaint();

            Thread.sleep(16);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.sync();

        int offsetY = rocket.getY() - (int) toolkit.getScreenSize().getHeight() / 2;

        if (!guiHandler.getStarted()) {
            drawImage(
                g2d, 
                startscreenImage,
                0,
                0,
                PANEL_WIDTH, 
                PANEL_HEIGHT
            );
        } else {
            int planetOffset = -offsetY - 50;

            drawImage(
                g2d, 
                backgroundImage, 
                0, 
                0, 
                PANEL_WIDTH + 100, 
                PANEL_HEIGHT + 240
            );
            drawImage(
                g2d,
                backgroundImage,
                0,
                planetOffset,
                PANEL_WIDTH + 100,
                PANEL_HEIGHT + 170
            );
        }

        if (guiHandler.getStarted()) {
            for (GameImage gameImage : images) {
                gameImage.render(g2d, offsetY);
            }
            rocket.render(g2d);
        }
    }

    public void generateImages() {
        int y = randomYPosition();
        boolean rightSide = rightSide();

        for (int i = 0; i < images.length; i++) {
            int x = randomXPosition();
            
            if (rightSide) {
                x += PANEL_WIDTH * 2/3;
            }
            
            images[i] = new GameImage(x, y, randomImage());
            y -= randomYDistance();
            rightSide = rightSide();
        }
    }

    public int randomXPosition() {
        int MAX = PANEL_WIDTH * 1/3 - 100;
        int MIN = 35;
        return (int) (Math.random() * (MAX - MIN)) + MIN;
    }

    public int randomYPosition() {
        int MAX = 450;
        int MIN = 0;
        return (int) (Math.random() * (MAX - MIN)) + MIN;
    }

    public int randomYDistance() {
        int MAX = 3000;
        int MIN = 2000;
        return (int) (Math.random() * (MAX - MIN)) + MIN;
    }

    public boolean rightSide() {
        return Math.random() * 100 > 50;
    }

    public BufferedImage randomImage() {
        BufferedImage[] gameImages = {
            asteriodImage,
            constalationImage,
            meteorImage,
            planetImage
        };

        return gameImages[(int) (Math.random() * gameImages.length)];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyHeldDown || started || guiHandler.getUiPaused()) { return; }

        if (e.getKeyCode() == 27) {
            guiHandler.setUiState("exitScreen");       
            return;
        }

        rocket.setUpLaunch();
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

    private void drawImage(Graphics g, BufferedImage image, int x, int y, int width, int height) {
        g.drawImage(
            image,
            x,
            y,
            width,
            height,
            null
        );
    }

    private class GameImage {
        
        private final int x, y;
        private final BufferedImage image;

        public GameImage(int x, int y, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.image = image;
        }

        public void render(Graphics g, int offset) {
            g.drawImage(
                image, 
                x - image.getWidth(),
                y - offset, 
                null
            );
        }

    }
}
