import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public class GuiHandler extends JPanel implements Runnable {
    
    private final int PANEL_WIDTH, PANEL_HEIGHT;
    private String uiState;

    public GuiHandler() {
        super();
        // Game panel to edit game UI
        this.PANEL_WIDTH = getSize().width;
        this.PANEL_HEIGHT = getSize().height;
        this.uiState = "startScreen";
        System.out.print(PANEL_HEIGHT);
        System.out.print(" ");
        System.out.print(PANEL_WIDTH);
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            startScreen();
            renderPlayableGame();
            try {
                Thread.sleep(1000/60);
            } catch(InterruptedException error) {
                System.err.println(error);
            }
        } 
    }

    public void startScreen() {
        if (!uiState.equals("startScreen")) {
            return;
        }
        // Background: will be implemented soon
        
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
        int centerX = (PANEL_WIDTH / 2) - 780 / 2;
        int centerY = PANEL_HEIGHT / 2;
        startButton.setBounds(centerX, centerY, 780, 300);
        add(startButton);
        uiState = "rendered";
    }
    
    public void renderPlayableGame() {
        if (!uiState.equals("playableGame")) {
            return;
        }

        JButton butt = new JButton("Butt");
        butt.setBounds(PANEL_WIDTH / 2, 300, 70, 30);
        add(butt);
        uiState = "rendered";
    }

}
