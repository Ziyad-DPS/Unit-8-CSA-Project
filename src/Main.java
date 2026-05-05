import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

// Main class that runs the program
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Creating instance of a JFrame and a JPanel
        JFrame frame = new JFrame("CSA Unit 8 Project");
        
        // the GuiHandler extends JPanel so add set GuiHandler as the contentPane
        GuiHandler guiHandler = new GuiHandler(frame);
        Game game = new Game(guiHandler);

        frame.add(game);
        
        // Create a new thread to run in parallel with the main gameloop
        Thread guiThread = new Thread(guiHandler);
        guiThread.start();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Exit on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setUndecorated(true);
        
        frame.setSize(screenSize.width, screenSize.height);
        // making the frame visible
        frame.pack();
        frame.setVisible(true);

        //starts the game
        game.initGame();
    }
}