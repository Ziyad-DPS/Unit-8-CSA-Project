public class Player {
    // SET TO CHANGE!
    // Basic class for the player storing their games data and handling all of their inputs

    private final GuiHandler guiHandler;
    private final Rocket rocket;

    public Player(GuiHandler guiHandler) {
        /*
            When the state of the player changes
            we need the GuiHandler to be able to render
            what the player wants
        */
        this.guiHandler = guiHandler;
        this.rocket = new Rocket();
        beginGame();
    }

    private void beginGame() {
        // Render the game so you can play
        guiHandler.renderPlayableGame();
    }

} 
