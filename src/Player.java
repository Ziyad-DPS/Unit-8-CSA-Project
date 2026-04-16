public class Player {
    // SET TO CHANGE!
    // Basic class for the player storing their games data and handling all of their inputs

    private final GuiHandler guiHandler;
    private final Rocket rocket;
    private int spaceBucks;
    private int durabilityPrice=100;
    private int powerPrice=100;
    private int fuelCapacityPrice=100;


    public Player(GuiHandler guiHandler) {
        /*
            When the state of the player changes
            we need the GuiHandler to be able to render
            what the player wants
        */
        this.guiHandler = guiHandler;
        this.rocket = new Rocket();
    }

    public void setBucks(int newAmnt){
        spaceBucks=newAmnt;
    }

    public int getBucks(){
        return spaceBucks;
    }

    public boolean buyDur(){
        if(spaceBucks>=durabilityPrice){
            increasePrice(1);
            return true;
        }
            return false;
    }

    public void increasePrice(int type){
        if(type==1){
            durabilityPrice*=1.5;
        }else if(type==2){
            powerPrice*=1.5;
        }else if(type==3){
            fuelCapacityPrice*=1.5;
        }
    }
} 
