import javax.swing.*;

// Main class
public class Main {

    // Main driver method
    public static void main(String[] args) throws InterruptedException {
        
        // Creating instance of JFrame
        JFrame frame = new JFrame("CSA Unit 8 Project");

        // using no layout managers
        frame.setLayout(null);
        
        // making the frame visible
        frame.setVisible(true);
        
        // Exit on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Make the JFrame windowed fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        GuiHandler gui = new GuiHandler(frame);
        Player player = new Player(gui);
        gui.startScreen();
        int i = 0;
        
        // Game loop
        // https://gameprogrammingpatterns.com/game-loop.html link for a game loop introduction
        while (true) {
            frame.repaint();
            Thread.sleep(1/60);
            i++;
        }
    
    }
}