import java.awt.Color;

import javax.swing.*;

public class GuiHandler {
    
    private final JFrame frame;

    public GuiHandler(JFrame frame) {
        this.frame = frame;
    }

    public void startScreen() {
        // Background:
        
        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.setBackground(new Color(0, 146, 242));
        startButton.setBounds(0, 0, 700, 300);
        frame.add(startButton);
    }

}
