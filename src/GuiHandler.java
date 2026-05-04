import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

public class GuiHandler extends JPanel implements Runnable {

    private final JFrame frame;
    private final int PANEL_WIDTH, PANEL_HEIGHT;
    private Rocket rocket;
    private String uiState;
    private boolean uiPaused, started;
    private final Color BUTTON_COLOR;
    private final Font BUTTON_FONT;
    private Player player;

    public GuiHandler(JFrame frame) {
        super(null);
        // Game panel to edit game UI
        // Get screen size from Toolkit\
        this.frame = frame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.PANEL_WIDTH = (int) screenSize.getWidth();
        this.PANEL_HEIGHT = (int) screenSize.getHeight();
        this.rocket = null;
        this.player = null;
        this.uiState = "startScreen";
        this.uiPaused = true;
        this.started = false;

        this.BUTTON_COLOR = new Color(0, 146, 242);
        this.BUTTON_FONT = new Font("Arial", Font.BOLD, 64);

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
        startButton.setBounds(PANEL_WIDTH / 2 - 375, PANEL_HEIGHT / 2 - 80, 750, 160);
        add(startButton);
        uiState = "rendered";
        uiPaused = true;
    }
    
    public void renderPlayableGame() {
        if (!uiState.equals("playableGame")) {
            return;
        }

        JLabel label = new JLabel(rocket.getDistance() + "");
        label.setBounds(35, PANEL_HEIGHT - 450, 2000, 300);
        label.setFont(BUTTON_FONT);
        label.setForeground(Color.WHITE);
        label.setName("distanceLabel");

        JLabel money = new JLabel(player.getSpaceBucks() + "");
        money.setBounds(35, PANEL_HEIGHT - 350, 2000, 300);
        money.setFont(BUTTON_FONT);
        money.setForeground(Color.WHITE);
        money.setName("moneyLabel");

        JButton upgradeScreenButton = new JButton("Upgrades");
        upgradeScreenButton.setBackground(BUTTON_COLOR);
        upgradeScreenButton.setFont(BUTTON_FONT);
        // Positioning of the button being centered horizontaly and going in the bottom
        upgradeScreenButton.setBounds(35, PANEL_HEIGHT - 150, 750, 130);
        
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
        
        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(PANEL_WIDTH / 2 - 200, PANEL_HEIGHT / 2 - 75, 400, 100);
        exitButton.setBackground(BUTTON_COLOR);
        exitButton.setFont(BUTTON_FONT);

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }
        });

        JButton resumeButton = new JButton("RESUME");
        resumeButton.setBounds(PANEL_WIDTH / 2 - 200, PANEL_HEIGHT / 2 + 75, 400, 100);
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

        JPanel background = new JPanel(null);
        background.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        background.setBackground(new Color(0, 0, 0, 0));

        
        JButton closeButton = new JButton("CLOSE");
        closeButton.setBounds(PANEL_WIDTH / 2 - 300, PANEL_HEIGHT - 260, 600, 120);
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
        }
        
        background.add(closeButton);
        add(background);
        uiState = "rendered";
    }
    
    public JPanel createCard(JPanel background, int position) {
        String[] cardTypes = {"fuel", "durability", "power"};
        
        String cardType = cardTypes[position];
        
        int margin = 100;
        int cardsWidth = 470;
        int cardsHeight = 670;
        int upgradeCardsX = PANEL_WIDTH / 4 - cardsWidth / 2 + margin;
        int upgradeCardsY = PANEL_HEIGHT / 2 - cardsHeight / 2 - margin;
        Color cardBackground = new Color(3, 86, 252);

        int upgradeButtonX = 35;
        int upgradeButtonY = 45;
        int buttonWidth = 400;
        int buttonHeight = 100;
        
        int offset = cardsWidth * position + margin * position;

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

        JPanel card = new JPanel(null);
        
        card.setBounds(
            upgradeCardsX + offset,
            upgradeCardsY,
            cardsWidth, 
            cardsHeight
        );

        card.setBackground(cardBackground);

        JButton upgradeButton = new JButton("UPGRADE");
        upgradeButton.setBounds(
            upgradeButtonX, 
            cardsHeight - buttonHeight - margin,
            buttonWidth, 
            buttonHeight
        );
        upgradeButton.setBackground(BUTTON_COLOR);
        upgradeButton.setFont(BUTTON_FONT);
        
        
        JLabel header = new JLabel(capitalizeString(cardType), JLabel.CENTER);
        header.setBounds(
            upgradeButtonX,
            upgradeButtonY,
            buttonWidth,
            buttonHeight
        );
        header.setForeground(Color.white);
        header.setFont(BUTTON_FONT);
        
        
        JLabel costText = new JLabel("COST: $" + costData, JLabel.CENTER);
        costText.setBounds(
            upgradeButtonX,
            upgradeButtonY + margin / 2 + 32,
            buttonWidth,
            buttonHeight
        );
        costText.setForeground(Color.white);
        costText.setFont(new Font("Arial", Font.BOLD, 32));
        
        JLabel levelText = new JLabel("LEVEL: " + levelData, JLabel.CENTER);
        levelText.setBounds(
            upgradeButtonX,
            upgradeButtonY + margin / 2 + 64,
            buttonWidth,
            buttonHeight
        );
        levelText.setForeground(Color.white);
        levelText.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel description = new JLabel(descriptionText);
        description.setBounds(
            upgradeButtonX,
            upgradeButtonY + margin + 128,
            buttonWidth,
            buttonHeight
        );
        description.setForeground(Color.white);
        description.setFont(new Font("Arial", Font.BOLD, 30));
        
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
        
        card.add(upgradeButton);
        card.add(header);
        card.add(costText);
        card.add(levelText);
        card.add(description);
        
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
