import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

public class GuiHandler extends JPanel implements Runnable {

    private final int PANEL_WIDTH, PANEL_HEIGHT;
    private String uiState;

    public GuiHandler() {
        super(null);
        // Game panel to edit game UI
        // Get screen size from Toolkit
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        this.uiState = "startScreen";
        setPreferredSize(screenSize);
    }

    @Override
    public void run() {
        setOpaque(false);
        boolean running = true;
        while (running) {
            repaint();
            startScreen();
            renderPlayableGame();

            try {
                Thread.sleep(16);
            } catch(InterruptedException error) {
                System.err.println(error);
            }
        } 
    }

    public void startScreen() {
        if (!uiState.equals("startScreen")) {
            return;
        }
        
        // Start button
        JButton startButton = new JButton("Start Game");
        
        // startButton action to initalize the game
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Create a Player object 
                uiState = "playableGame";
                removeAll();
            }   
        });
        
        // The styles of the button
        startButton.setBackground(new Color(0, 146, 242));
        startButton.setFont(new Font("Arial", Font.BOLD, 64));
        // Positioning of the button being centered horizantaly and going in the bottom
        startButton.setBounds(PANEL_WIDTH / 2 - 250, PANEL_HEIGHT / 2 - 30, 500, 60);
        add(startButton);
        uiState = "rendered";
    }
    
    public void renderPlayableGame() {
        if (!uiState.equals("playableGame")) {
            return;
        }

        JButton butt = new JButton("Butt");
        butt.setBounds(PANEL_WIDTH - 700, PANEL_HEIGHT / 2, 70, 30);
        add(butt);
        uiState = "rendered";
    }
}
