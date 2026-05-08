//imports important methods and objects
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
    //creates vars and objects to be used later
    private int PANEL_WIDTH, PANEL_HEIGHT, MOON_DISTANCE;
    private double scaleX, scaleY;
    
    private GuiHandler guiHandler;
    private Player player;
    private Rocket rocket;
    private BufferedImage startscreenImage, backgroundImage, asteriodImage, constalationImage, meteorImage, moonImage, planetImage;
    private GameImage[] images;

    private boolean keyHeldDown, started;
    
    public Game(GuiHandler guiHandler) {
        // https://www.geeksforgeeks.org/java/java-jlayeredpane/ link to learn about JLayeredPane
        //makes window full screen and sets certain variables and objects
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        this.MOON_DISTANCE = -5000; // Experimental set to change

        this.scaleX = (double) PANEL_WIDTH / 2560;
        this.scaleY = (double) PANEL_HEIGHT / 1600;
        
        this.guiHandler = guiHandler;
        this.player = new Player(); 
        this.rocket = new Rocket(player);

        guiHandler.setRocket(rocket);
        guiHandler.setPlayer(player);

        this.keyHeldDown = false;
        this.started = false;

        //renders images and reports if there was an error
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

    //loops to keep repainting the screen
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
        //renders the images
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.sync();

        int offsetY = rocket.getY() - (int) PANEL_HEIGHT / 2;
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
            int planetOffset = -offsetY - (int) (120 * scaleY);
            int padding = (int) (100 * scaleX);
            int moonWidth = 912;
            int moonHeight = 780;
            int moonPosition = (int) (PANEL_WIDTH / 2 - moonWidth / 2 * scaleX);

            drawImage(
                g2d, 
                backgroundImage, 
                0, 
                0, 
                PANEL_WIDTH + padding, 
                PANEL_HEIGHT + (int) (240 * scaleY)
            );
            drawImage(
                g2d,
                backgroundImage,
                0,
                planetOffset,
                PANEL_WIDTH + padding,
                PANEL_HEIGHT + (int) (240 * scaleY)
            );
            drawImage(
                g2d,
                moonImage,
                moonPosition,
                MOON_DISTANCE - offsetY,
                (int) (moonWidth * scaleX), 
                (int) (moonHeight * scaleY)
            );
        }

        if (guiHandler.getStarted()) {
            for (GameImage gameImage : images) {
                gameImage.render(g2d, (int) (offsetY * scaleY));
            }
            rocket.render(g2d);
        }
    }

    public void generateImages() {
        //randomly adds images to screens
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
        //returns random x value
        int MAX = PANEL_WIDTH * 1/3 - (int) (100 * scaleX);
        int MIN = 35;
        return (int) (Math.random() * (MAX - MIN)) + MIN;
    }

    public int randomYPosition() {
        //returns random starting y value for images
        int MAX = 450;
        int MIN = 0;
        return (int) (Math.random() * (MAX - MIN)) + MIN;
    }

    public int randomYDistance() {
        //returns random distance between images
        int MAX = 3000;
        int MIN = 2000;
        return (int) (Math.random() * (MAX - MIN)) + MIN;
    }

    public boolean rightSide() {
        //returns a random boolean for what side to place the image
        return Math.random() * 100 > 50;
    }

    public BufferedImage randomImage() {
        //bufferedImage array to hold all the images
        BufferedImage[] gameImages = {
            asteriodImage,
            constalationImage,
            meteorImage,
            planetImage
        };

        //returns a random image from gameImages array
        return gameImages[(int) (Math.random() * gameImages.length)];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //checks if a key is pressed
        if (keyHeldDown || started || guiHandler.getUiPaused()) { return; }

        if (e.getKeyCode() == 27) {
            guiHandler.setUiState("exitScreen");       
            return;
        }
        //launches the rocket and sets keyHeldDown to true
        rocket.setUpLaunch();
        keyHeldDown = true;
    }

    @Override
    //sets keyHeldDown to false
    public void keyReleased(KeyEvent e) {
        keyHeldDown = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }
//draws the image selected previously
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
        //initializes ints and images
        private final int x, y;
        private final BufferedImage image;

        //sets ints and images
        public GameImage(int x, int y, BufferedImage image) {
            this.x = (int) (x * scaleX);
            this.y = (int) (y * scaleY);
            this.image = image;
        }

        //draws image
        public void render(Graphics g, int offset) {
            g.drawImage(
                image, 
                x - (int) (image.getWidth() * scaleX),
                y - offset,
                null
            );
        }

    }
}
