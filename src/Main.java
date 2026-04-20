import javax.swing.*;

// Main class that runs the program
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Creating instance of a JFrame and a JPanel
        JFrame frame = new JFrame("CSA Unit 8 Project");
        
        // the GuiHandler extends JPanel so add set GuiHandler as the contentPane
        GuiHandler guiHandler = new GuiHandler();
        
        // Create a new thread to run in parallel with the main gameloop
        Thread guiThread = new Thread(guiHandler);
        guiThread.start();
        
        // Exit on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Game game = new Game(guiHandler);

        frame.setContentPane(game);
        frame.add(guiHandler);

        // making the frame visible
        frame.setVisible(true);
        // Make the JFrame windowed fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        game.initGame();
    }
}