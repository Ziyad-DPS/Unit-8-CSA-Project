public class Player {
    // Basic class for the player storing their games data

    //initializes variables to be used later
    private int spaceBucks;
    private int durabilityPrice;
    private int powerPrice;
    private int fuelCapacityPrice;
    private int durLevel;
    private int powLevel;
    private int fuelLevel;
    private int[] distance = {500, 1500, 5000, 12000, 25000, 50000, 90000, 140000, 195000, 239000};

//sets variables to specific values
    public Player() {
        this.durabilityPrice = 100;
        this.powerPrice = 100;
        this.fuelCapacityPrice = 100;
        this.durLevel = 1;
        this.powLevel = 1;
        this.fuelLevel = 1;
    }

//if the player's spacebucks amount it increases price of spacebucks it costs for the upgrade
//and decreases the spacebucks amount before adding 1 to the durability level, also returns true or false if the upgrade was purchased
    public boolean buyDur(){
        if(spaceBucks>=durabilityPrice){
            spaceBucks-=durabilityPrice;
            increasePrice(1);
            durLevel++;
            return true;
        }else{
            return false;
        }
    }

    //if the player's spacebucks amount it increases price of spacebucks it costs for the upgrade
//and decreases the spacebucks amount before adding 1 to the power level, also returns true or false if the upgrade was purchased
    public boolean buyPow(){
        if(spaceBucks>=powerPrice){
            spaceBucks-=powerPrice;
            increasePrice(2);
            powLevel++;
            return true;
        }else{
            return false;
        }  
    }

    //if the player's spacebucks amount it increases price of spacebucks it costs for the upgrade
//and decreases the spacebucks amount before adding 1 to the fuel level, also returns true or false if the upgrade was purchased
    public boolean buyFuel(){
        if(spaceBucks>=fuelCapacityPrice){
            spaceBucks-=fuelCapacityPrice;
            increasePrice(3);
            fuelLevel++;
            return true;
        }else{
            return false;
        }
    }

    //increases the price of upgrades depending on the type parameter
    public void increasePrice(int type){
        if(type==1){
            durabilityPrice *= 1.5;
        }else if(type==2){
            powerPrice *= 1.5;
        }else if(type==3){
            fuelCapacityPrice *= 1.5;
        }
    }

    //Checks what the lowest upgrade level is before returning the corresponding number found in the distance array
    public int getDistance(){
        if(powLevel<fuelLevel&&powLevel<durLevel){
            return distance[powLevel];
        }else if(fuelLevel<powLevel&&fuelLevel<durLevel){
        return distance[fuelLevel];
        }else if(durLevel<powLevel&&durLevel<fuelLevel){
            return distance[durLevel];
        }else{
            return -1;
        }
    }

    //increases spacebucks based on the distance the player travels
    public void distanceToSpaceBucks(int distance){
    spaceBucks  += (int) (125000/239000)*(2*distance);
    }

    //returns fuel level
    public int getFeulLevel() {
        return fuelLevel;
    }
} 
