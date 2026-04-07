import javax.swing.*;

// Main class
public class Main {

    // Main driver method
    public static void main(String[] args) {
        
        // Creating instance of JFrame
        JFrame frame = new JFrame("CSA Unit 8 Project");

        // Make the JFrame windowed fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);
        
        // Exit on close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    }
}