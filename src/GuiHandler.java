import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

public class GuiHandler extends JPanel implements Runnable {

    private final JFrame frame;
    private final int PANEL_WIDTH, PANEL_HEIGHT;
    private final double uiScaleX, uiScaleY;
    private Rocket rocket;
    private String uiState;
    private boolean uiPaused, started;
    private final Color BUTTON_COLOR;
    private final Font BUTTON_FONT;
    private Player player;

    public GuiHandler(JFrame frame) {
        super(null);
        // Game panel to edit game UI
        // Get screen size from Toolkit
        this.frame = frame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        this.uiScaleX = (double) PANEL_WIDTH / 2560;
        this.uiScaleY = (double) PANEL_HEIGHT / 1600;
        this.rocket = null;
        this.player = null;
        this.uiState = "startScreen";
        this.uiPaused = true;
        this.started = false;

        this.BUTTON_COLOR = new Color(0, 146, 242);
        this.BUTTON_FONT = new Font("Arial", Font.BOLD, (int) (64 * uiScaleX));

        setPreferredSize(screenSize);
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            repaint();
            startScreen();
            renderPlayableGame();
            displayDistance();
            upgradeScreen();
            exitScreen();
            setOpaque(false);

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
        int sizeX = (int) (750 * uiScaleX);
        int sizeY = (int) (160 * uiScaleY);
        JButton startButton = new JButton("Start Game");
        
        // startButton action to initalize the game
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Create a Player object 
                uiState = "playableGame";
                uiPaused = false;
                started = true;
                removeAll();
            }   
        });
        
        // The styles of the button
        startButton.setBackground(BUTTON_COLOR);
        startButton.setFont(BUTTON_FONT);
        // Positioning of the button being centered horizontaly and going in the bottom
        startButton.setBounds(PANEL_WIDTH / 2 - (int) (375 * uiScaleX), PANEL_HEIGHT / 2 - (int) (80 * uiScaleY), sizeX, sizeY);
        add(startButton);
        uiState = "rendered";
        uiPaused = true;
    }
    
    public void renderPlayableGame() {
        if (!uiState.equals("playableGame")) {
            return;
        }

        int labelWidth = (int) (2000 * uiScaleX);
        int labelHeight = (int) (300 * uiScaleX);
        int buttonWidth = (int) (750 * uiScaleX);
        int buttonHeight = (int) (130 * uiScaleY);
        int xPosition = (int) (35 * uiScaleX);

        JLabel label = new JLabel(rocket.getDistance() + "");
        label.setBounds(xPosition, PANEL_HEIGHT - (int) (450 * uiScaleY), labelWidth, labelHeight);
        label.setFont(BUTTON_FONT);
        label.setForeground(Color.WHITE);
        label.setName("distanceLabel");

        JLabel money = new JLabel(player.getSpaceBucks() + "");
        money.setBounds(xPosition, PANEL_HEIGHT - (int) (350 * uiScaleY), labelWidth, labelHeight);
        money.setFont(BUTTON_FONT);
        money.setForeground(Color.WHITE);
        money.setName("moneyLabel");

        JButton upgradeScreenButton = new JButton("Upgrades");
        upgradeScreenButton.setBackground(BUTTON_COLOR);
        upgradeScreenButton.setFont(BUTTON_FONT);
        // Positioning of the button being centered horizontaly and going in the bottom
        upgradeScreenButton.setBounds(xPosition, PANEL_HEIGHT - (int) (150 * uiScaleY), buttonWidth, buttonHeight);
        
        upgradeScreenButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                uiPaused = true;
                uiState = "upgradeScreen";
                removeAll();
            }
        });

        add(upgradeScreenButton);
        add(money);
        add(label);
        uiState = "rendered";
    }
    
    public void exitScreen() {
        if (!uiState.equals("exitScreen")) {
            return;
        }
        uiPaused = true;

        int sizeX = (int) (400 * uiScaleX);
        int sizeY = (int) (100 * uiScaleY);
        int margin = sizeY / 2 + (int) (75 * uiScaleY);
        
        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(PANEL_WIDTH / 2 - sizeX / 2, PANEL_HEIGHT / 2 - margin, sizeX, sizeY);
        exitButton.setBackground(BUTTON_COLOR);
        exitButton.setFont(BUTTON_FONT);

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }
        });

        JButton resumeButton = new JButton("RESUME");
        resumeButton.setBounds(PANEL_WIDTH / 2 - sizeX / 2, PANEL_HEIGHT / 2, sizeX, sizeY);
        resumeButton.setBackground(BUTTON_COLOR);
        resumeButton.setFont(BUTTON_FONT);
        
        resumeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                uiState = "playableGame";
                uiPaused = false;
                removeAll();
            }
        });
        
        add(resumeButton);
        add(exitButton);
        uiState = "rendered";
    }

    public void upgradeScreen() {
        if (!uiState.equals("upgradeScreen")) {
            return;
        }

        uiPaused = true;

        JPanel background = new JPanel();
        background.setLayout(new FlowLayout(FlowLayout.CENTER));
        background.setBounds(0, PANEL_HEIGHT / 2 - PANEL_HEIGHT / 4, PANEL_WIDTH, PANEL_HEIGHT);
        background.setBackground(new Color(0, 0, 0, 0));
        background.setAlignmentY(CENTER_ALIGNMENT);

        int buttonX = (int) (PANEL_WIDTH / 2 - 300 * uiScaleX);
        int buttonY = (int) (PANEL_HEIGHT - 260 * uiScaleY);
        JButton closeButton = new JButton("CLOSE");
        closeButton.setBounds(buttonX, buttonY, (int) (600 * uiScaleX), (int) (120 * uiScaleY));
        closeButton.setBackground(BUTTON_COLOR);
        closeButton.setFont(BUTTON_FONT);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                uiState = "playableGame";
                uiPaused = false;
                removeAll();
            }
        });
        
        int amountOfCards = 3;
        JPanel card = null;
        for (int i = 0; i < amountOfCards; i++) {
            card = createCard(background, i);
            background.add(card);
            ((FlowLayout)background.getLayout()).setHgap((int) (80 * uiScaleX));
        }
        
        add(closeButton);
        add(background);
        uiState = "rendered";
    }
    
    public JPanel createCard(JPanel background, int position) {
        String[] cardTypes = {"fuel", "durability", "power"};
        
        String cardType = cardTypes[position];
        
        int levelData, costData;
        String descriptionText;

        if (cardType.equals("fuel")) {
            levelData = player.getFuelLevel();
            costData = player.getFuelPrice();
            descriptionText = "<html>Improves fuel capacity<br/>allowing for more<br/>time in space.<html>";
        }
        else if (cardType.equals("durability")) {
            levelData = player.getDurabilityLevel();
            costData = player.getDurabilityPrice();
            descriptionText = "<html>Increases durability<br/>and sustanibility for<br/> rocket while in space.<html>";
        }
        else {
            levelData = player.getPowerLevel();
            costData = player.getPowerPrice();
            descriptionText = "<html>Increases the rockets<br/>power for faster travel<br/> speeds in space.<html>";
        }

        int cardsWidth = (int) (470 * uiScaleX);
        int cardsHeight = (int) (670 * uiScaleY);
        Color cardBackground = new Color(3, 86, 252);

        int buttonWidth = (int) (400 * uiScaleX);
        int buttonHeight = (int) (100 * uiScaleY);
        int fontSize = (int) (32 * uiScaleX);
        Dimension buttonDimension = new Dimension(buttonWidth, buttonHeight);
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
        card.setPreferredSize(
            new Dimension(cardsWidth, cardsHeight)
        );
        
        card.setBackground(cardBackground);
        
        JButton upgradeButton = new JButton("UPGRADE");
        upgradeButton.setPreferredSize(
            buttonDimension
        );
        upgradeButton.setBackground(BUTTON_COLOR);
        upgradeButton.setFont(BUTTON_FONT);
        
        
        JLabel header = new JLabel(capitalizeString(cardType), JLabel.CENTER);
        header.setPreferredSize(
            buttonDimension
        );
        header.setForeground(Color.white);
        header.setFont(BUTTON_FONT);
        
        
        JLabel costText = new JLabel("COST: $" + costData, JLabel.CENTER);
        costText.setPreferredSize(
            buttonDimension
        );
        costText.setForeground(Color.white);
        costText.setFont(new Font("Arial", Font.BOLD, fontSize));
        
        JLabel levelText = new JLabel("LEVEL: " + levelData, JLabel.CENTER);
        levelText.setPreferredSize(
            buttonDimension
        );
        levelText.setForeground(Color.white);
        levelText.setFont(new Font("Arial", Font.BOLD, fontSize));

        JLabel description = new JLabel(descriptionText, JLabel.CENTER);
        description.setPreferredSize(
            buttonDimension
        );
        description.setForeground(Color.white);
        description.setFont(new Font("Arial", Font.BOLD, (int) (30 * uiScaleX)));
        
        upgradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cardType.equals("fuel")) {
                    player.buyFuel();
                    costText.setText("COST: $" + player.getFuelPrice());
                    levelText.setText("LEVEL: " + player.getFuelLevel());
                } 
                else if (cardType.equals("durability")) {
                    player.buyDur();
                    costText.setText("COST: $" + player.getDurabilityPrice());
                    levelText.setText("LEVEL: " + player.getDurabilityLevel());
                } 
                else {
                    player.buyPow();
                    costText.setText("COST: $" + player.getPowerPrice());
                    levelText.setText("LEVEL: " + player.getPowerLevel());
                }

                uiState = "upgradeScreen";
            }
        });
        
        Dimension gapeOne = new Dimension(0, (int) (50 * uiScaleY));
        Dimension gapeTwo = new Dimension(0, (int) (35 * uiScaleY));

        card.add(Box.createRigidArea(gapeOne));
        card.add(header);
        card.add(Box.createRigidArea(gapeTwo));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(costText);
        card.add(Box.createRigidArea(new Dimension(0, (int) (5 * uiScaleY))));
        costText.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(levelText);
        card.add(Box.createRigidArea(gapeTwo));
        levelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(description);
        card.add(Box.createRigidArea(new Dimension(0, (int) (65 * uiScaleY))));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(upgradeButton);
        card.add(Box.createRigidArea(gapeOne));
        upgradeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return card;
    }
    
    public void displayDistance() {
        for (Component c : getComponents()) {
            if (c instanceof JLabel) {
                JLabel d = (JLabel) c;
                
                if (d.getName() == null) { continue; }

                if (d.getName().equals("distanceLabel")) {
                    d.setText("Distance: " + rocket.getDistance() + "MI");
                }

                if (d.getName().equals("moneyLabel")) {
                    d.setText("Spacebucks: $" + player.getSpaceBucks());
                }
            }
        }
    }

    public String capitalizeString(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public boolean getStarted() {
        return started;
    }
    
    public boolean getUiPaused() {
        return uiPaused;
    }

    public void setUiState(String uiState) {
        this.uiState = uiState;
    }

    public void setRocket(Rocket rocket) {
        this.rocket = rocket;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
