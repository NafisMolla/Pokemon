//Pokemon.java
//Nafis Molla
//This is the Pokemon class, where we control everything that a Pokemon is.
import sun.font.TrueTypeFont;

import java.util.*;
import java.io.*;

public class Pokemon {
    private String name;   //all private variables that describe the pokemons characteristics
    private int hp;
    private int energy;
    private String type;
    private String resistance;
    private String weakness;
    private int orgHp;

    private boolean stun = false;
    private boolean disable = false;  //flags for disable and stun
    private boolean beendisabled = false;

    private Attack[]attacks;  //holds the attacks that he pokemon has available to it
    private ArrayList<Attack>avaliableattacks = new ArrayList<Attack>();

    public ArrayList<Attack> validAttacks(){ //what attacks are valid function
        int n=0;
        ArrayList<Attack>valid = new ArrayList<Attack>(); //array that holds valid attacks

        for(Attack a : attacks) {
            if (a.cost <= energy) {  //cheeks to see if pokemon has enough energy
                valid.add(a);
            }
        }
        return valid; //returns valid atacks
    }


    public Pokemon(String info){ //constructor
        String [] stats = info.split(",");
        name = stats[0];
        hp = Integer.parseInt(stats[1]);
        orgHp = Integer.parseInt(stats[1]); //reading pokemon characteristics from pokemontxt.txt and setting those values to the corresponding variable
        energy = 50;
        type = stats[2];
        resistance = stats[3];
        weakness = stats[4];



        int numatt = Integer.parseInt((stats[5]));  //indicates where the attacks start and how many attacks the pokemon has available to it

        attacks = new Attack[numatt]; //declaring a primitive Attack type list

        for(int i=0;i<numatt;i++){
            String atName = stats[6+4*i];
            String atspec = stats[9+4*i];
            int atcost = Integer.parseInt(stats[7+4*i]);  //getting all the stats for the pokemons attacks
            int atdamg = Integer.parseInt(stats[8+4*i]);

            Attack att = new Attack(atName,atspec,atcost,atdamg); //creating and Attack Object
            attacks[i] = att; //adding Attack object to attack list


        }


    }

    public void attack(Pokemon bad, Attack att){  //attack function

        double extraD = getResistance_weakeness(bad); //gets damage multiplier depending on resistance or weakness
        ////////////////////////////////////////////////////////////////////
        if((att.special).equals("stun")){ //stun special
            int RNG = rand(0,1);//random RNG generator(generates in between 0 and including 1)
            if(RNG==0){ //stunned enemy
                bad.add_minus_HP("-",(att.damage+disabledamage())*extraD);//subtract health from Enemy
                energy -= att.cost;//subtracts energy from attacking pokemon
                System.out.println(name+": you stunned the enemy");
                System.out.printf("%s: has done %.2f damage \n",name,(att.damage+disabledamage())*extraD);
            }
            else{//attacker didnt get stun special
                bad.add_minus_HP("-",(att.damage+disabledamage())*extraD);
                energy-=att.cost;
                System.out.printf("%s: has done %1f damage \n",name,(att.damage+disabledamage())*extraD);

            }
        }
        //////////////////////////////////////////////////////////////////////////////////
        if((att.special).equals("wild card")){ //Wild Card special
            int RNG = rand(0,1); //RNG generator
            if(RNG==0){ //attacker gets wild carded
                System.out.printf("Sorry %s you got wild carded(no damage is done) \n", name);
                energy-=att.cost;
            }
            else{ //attacker didn't get wild carded
                bad.add_minus_HP("-",(att.damage+disabledamage())*extraD);//subtract health from Enemy
                energy-=att.cost;//subtract energy from attacker
                System.out.printf("%s: has done %.2f damage \n",name,(att.damage+disabledamage())*extraD);

            }

        }
        ///////////////////////////////////////////////////////////////////////////////////////////
        if((att.special).equals("wild storm")){ //wild storm special
            int RNG = rand(0,1); //RNG generator
            if(RNG==0){//does no damage
                System.out.println(name+" You got unlucky wild storm made it so you do zero damage");
            }

            else{ //gets wild storm special perk
                int count = 0; //counter for damage multiplier
                boolean cheek = true; //while loop running flag

                while(cheek) {
                    int rng = rand(0, 1); //rng generator

                    if (rng == 0) { //end wild storm attack
                        System.out.printf("you hit %s %d times \n", bad.getName(), count); //prints how many times attacker hit

                        cheek = false;
                    }

                    count++; // adds to counter for damage multiplier


                }
                bad.add_minus_HP("-",(((att.damage+disabledamage())*extraD)*count));// subtracting damage(damage multiplier included)
                energy-=att.cost;//subtracting energy according to attack cost
                System.out.printf("%s: has done %.2f damage \n",name,((att.damage+disabledamage())*extraD)*count);//printing how much damage was done

            }

        }

        if((att.special).equals("disable")){ //disable special
            if(!bad.getbeenDisabled()){ //if bad pokemon hasn't been disabled then we disable him because you can only disable once.
                bad.beendisabled = true; //setting been disabled flag to true
                bad.add_minus_HP("-",(att.damage+disabledamage())*extraD);//subtracting health from bad pokemon
                energy-=att.cost;//subtracting dame from attacking pokemon
                System.out.println(bad.getName()+" has been disabled all attacks will do 10 less damage"); //printing which pokemon got disabled
            }
            else{ //if already disabled then it's not gonna disable the pokeon again
                bad.add_minus_HP("-",(att.damage+disabledamage())*extraD);
                energy-=att.cost;
                System.out.printf("%s: has done %.2f damage \n",name,(att.damage+disabledamage())*extraD); //printing how much damage was done
            }

        }

        if((att.special).equals("recharge")){//recharge special
            giveEnergy(20); //gives 20 energy to attacking pokemon
            System.out.println(name+" you got 20 energy");
            bad.add_minus_HP("-",(att.damage+disabledamage())*extraD);//subtracting health from bad
            energy-=att.cost;//subtracting energy
            System.out.printf("%s: has done %.2f damage \n",name,(att.damage+disabledamage())*extraD);


        }

        if((att.special).equals(" ")){ //no special just a normal attack
            System.out.println(name+": you have no special");
            bad.add_minus_HP("-",(att.damage+disabledamage())*extraD);
            energy-=att.cost;
            System.out.printf("%s: has done %.2f damage \n",name,(att.damage+disabledamage())*extraD);

        }

    }

//////////////////////////////////////////////////////////////////////////////////////////////////

    public int disabledamage(){ //this function cheeks to see if the pokemon is disabled then returns damage adjustment
        if(beendisabled){
            return -10;
        }
        else{
            return 0;
        }
    }


    public static int rand(int low,int high){ //random number generator
        return (int)(Math.random()*(high-low+1)+(low)); //inclusive of high
    }
/////////////////////////////////////////////////////////////////////////////////////////////
    public double getResistance_weakeness(Pokemon bad){ //this function returns a integer value depending on weakness and resistance

        if(type.equals(bad.weakness)){
            return 2;
        }

        else if(type.equals(bad.resistance)){
            return 0.5;

        }

        else{
            return 1;
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean getbeenDisabled(){ //cheeks to see if a pokeon has been disabled
        return beendisabled;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean getDisable(){
        if(disable){
            return true;
        }
        else{
            return false;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setBeendisabled(boolean x){ //sets beendisabled flag
        beendisabled = x;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setStunned(boolean x) { //sets stun flag
        stun = x;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean getStuned() { //gets stun value
        if (stun) {
            return true;
        }
        else {
            return false;
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void giveEnergy(int x){    //gives energy to pokemon but does not let it go over 50
        energy = Math.min(energy+x,50);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setHP(double h){ //sets hp for pokemon
        h=hp;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void add_minus_HP(String k, double h){ //adds to hp or can also minus from hp
        if(k == "+"){
            hp += h;
        }

        if(k == "-"){
            hp-=h;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getHP(){ //gets hp
        return hp;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void heal(){ //heals to original hp
        hp = orgHp;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getName(){ //gets pokemon name
        return name;
    }

    public boolean isAlive(){ //cheeks and returns if pokemon is alive
        if(hp >0){
            return true;
        }
        else{
            return false;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isDead(){ //cheeks if pokemon is dead returns a boolean value
        boolean returning;
        if(hp<=0){
            returning = true;
        }
        else{
            returning = false;
        }
        return returning;
    }

    public int getEnergy(){ //gets energy
        return energy;
    }

    public void regen(int x){ //ads to hp but does not let the hp go over the original amount
        hp = Math.min(hp+x,orgHp);
    }

    public String getType(){ //returns pokemon type
        return type;
    }

    public String toString(){ //to string method

        return name + "  HP:"+hp+ "  ";
    }

}
