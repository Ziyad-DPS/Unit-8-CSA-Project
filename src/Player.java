public class Player {
    // Basic class for the player storing their games data

    private int spaceBucks;
    private int durabilityPrice;
    private int powerPrice;
    private int fuelCapacityPrice;
    private int durLevel;
    private int powLevel;
    private int fuelLevel;
    private int[] distance = {500, 1500, 5000, 12000, 25000, 50000, 90000, 140000, 195000, 239000};


    public Player() {
        this.durabilityPrice = 100;
        this.powerPrice = 100;
        this.fuelCapacityPrice = 100;
        this.durLevel = 1;
        this.powLevel = 1;
        this.fuelLevel = 1;
    }


    public boolean buyDur(){
        if(spaceBucks>=durabilityPrice){
            increasePrice(1);
            durLevel++;
            return true;
        }else{
            return false;
        }
    }

    public boolean buyPow(){
        if(spaceBucks>=powerPrice){
            increasePrice(2);
            powLevel++;
            return true;
        }else{
            return false;
        }  
    }

    public boolean buyFuel(){
        if(spaceBucks>=fuelCapacityPrice){
            increasePrice(3);
            fuelLevel++;
            return true;
        }else{
            return false;
        }
    }

    public void increasePrice(int type){
        if(type==1){
            durabilityPrice *= 1.5;
        }else if(type==2){
            powerPrice *= 1.5;
        }else if(type==3){
            fuelCapacityPrice *= 1.5;
        }
    }

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

    public void distanceToSpaceBucks(int distance){
    spaceBucks  += (int) (125000/239000)*(2*distance);
    }
} 
