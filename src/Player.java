public class Player {
    // SET TO CHANGE!
    // Basic class for the player storing their games data and handling all of their inputs

    private final GuiHandler guiHandler;
    private final Rocket rocket;
    private int spaceBucks;
    private int durabilityPrice=100;
    private int powerPrice=100;
    private int fuelCapacityPrice=100;
    private int durLevel=1;
    private int powLevel=1;
    private int fuelLevel=1;

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
            durLevel++;
            return true;
        }
            return false;
    }

    public boolean buyPow(){
        if(spaceBucks>=powerPrice){
            increasePrice(2);
            powLevel++;
            return true;
        }
            return false;
    }

    public boolean buyFuel(){
        if(spaceBucks>=fuelCapacityPrice){
            increasePrice(3);
            fuelLevel++;
            return true;
        }
            return false;
    }

    public void increasePrice(int type){
        if(type==1){
            durabilityPrice = (int) Math.pow(0.01609/5.191919, durabilityPrice);
        }else if(type==2){
            powerPrice = (int) Math.pow(0.01609/5.191919, powerPrice);
        }else if(type==3){
            fuelCapacityPrice = (int) Math.pow(0.01609/5.191919, fuelCapacityPrice);
        }
    }

    public void distanceToSpaceBucks(int distance){
    spaceBucks+=(int) (125000/239000)*distance;
    }
} 
