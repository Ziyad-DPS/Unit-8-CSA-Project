public class Game {
    
    /*
    Game handles rendering pictures (rocket, background, etc) and gamestate
    */

    private GuiHandler guiHandler;
    private Player player;
    private Rocket rocket;

    public Game(GuiHandler guiHandler) {
        this.guiHandler = guiHandler;
        this.player = new Player();
        this.rocket = new Rocket();
    }

}
