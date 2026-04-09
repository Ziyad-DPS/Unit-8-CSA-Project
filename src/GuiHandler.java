import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public class GuiHandler {
    
    private final JFrame frame;

    public GuiHandler(JFrame frame) {
        // Game frame to edit
        this.frame = frame;
    }

    public void startScreen() {
        // Background: will be implemented soon
        
        // Start button
        JButton startButton = new JButton("Start Game");
        // When we add a GuiHandler to the Player constructor in the mouseClicked function
        // it is in a new object (MouseAdapter) so we save this GuiHandler and pass it in to avoid
        // an error
        GuiHandler gui = this;

        // startButton action to initalize the game
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Create a Player object 
                Player player = new Player(gui);
                frame.getContentPane().removeAll();
                frame.repaint();
                System.out.println("hello");
            }   
        });

        // The styles of the button
        startButton.setBackground(new Color(0, 146, 242));
        startButton.setFont(new Font("Arial", Font.BOLD, 64));
        // Positioning of the button being centered horizantaly and going in the bottom
        int centerX = (frame.getContentPane().getSize().width / 2) - 780 / 2;
        int centerY = frame.getContentPane().getSize().height - 350;
        startButton.setBounds(centerX, centerY, 780, 300);
        frame.add(startButton);
        frame.repaint();
    }

    public void renderPlayableGame() {
        return;
    }

}
