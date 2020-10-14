//Attack.java
//Nafis Molla
//This is the Attack class that holds that organises all the attacks and related things all in one place.

import java.util.*;
public class Attack {

    public String name,special; //variables are public so they can be accessed by other classes easily
    public int cost, damage;

    public Attack (String n,String s,int c, int d){//constructor
        name = n;
        special = s;  ////all public variables that describe the pokemons attack characteristics
        cost = c;
        damage = d;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
    public String toString(){ //to string function
        return name+" "+special+" "+cost+" "+damage;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getNameCost(){ //prints the releavent information that an attacker need to see when picking an attack
        return name+" "+"this costs "+cost+" this does "+damage+" damage"+" this attacks special is "+special+".";
    }

}
