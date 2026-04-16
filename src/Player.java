public class Player {
    // Basic class for the player storing their games data

    private int spaceBucks;
    private int durabilityPrice;
    private int powerPrice;
    private int fuelCapacityPrice;
    private int durLevel;
    private int powLevel;
    private int fuelLevel;

    public Player() {
        this.durabilityPrice = 100;
        this.powerPrice = 100;
        this.fuelCapacityPrice = 100;
        this.durLevel = 1;
        this.powLevel = 1;
        this.fuelLevel = 1;
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
