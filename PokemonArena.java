//PokemonArena.java
//Nafis Molla
//This is the PokemonArena class. In this class is where all the battle and choosing of the pokemon happens


import com.sun.org.apache.regexp.internal.RE;
import jdk.nashorn.internal.ir.WhileNode;

import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
public class PokemonArena {
    private static ArrayList<Pokemon>allPokes = new ArrayList<Pokemon>(); //holds all the pokes
    private static ArrayList<Pokemon>userPokes = new ArrayList<>();//hold the 4 pokemon the user pick
/////////////////////////////////////////////////////////////////////////////////////
    public static final int USER=0;  //constants for turns
    public static final int COMPUTER=1;
////////////////////////////////////////////////////////////////

    public static final int ATTACK=55;  //constants for attack,retreat and pass
    public static final int RETREAT=66;
    public static final int PASS = 77;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[1;92m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW    COLORS FOR TEXT
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String RESET = "\033[0m";  // Text Reset




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int Turn; //declaring turn variable
    public static int battlecounter =1;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Pokemon curUser; //the current User pokemon
    public static Pokemon curComputer; // the current computer pokemon

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args){ //main loop

        welcomeMessage(); // prints a nice welcome message
        load(); // loads text file
        gameLoop(); //main game


    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void load(){ //loads in text file containing all the information about the 28 pokemons
        try{
            int runs = 0; //how many time the for loop will run
            Scanner fin = new Scanner(new BufferedReader(new FileReader("pokemontxt.txt"))); //reading file
            runs = Integer.parseInt(fin.nextLine());

            for(int i=0; i<runs; i++){
                allPokes.add(new Pokemon(fin.nextLine())); //adding all the pokemon to the allpokes Arraylist.
            }

        }
        catch(IOException ex){
            System.out.println("umm where is pokemontxt.txt"); //print this if file is not found
        }

        for(int i=0; i<28;i++){
            //printing all the pokemon names and relevant stats
            System.out.println(PURPLE+i + "."+" "+allPokes.get(i).getName()+"| Type:"+allPokes.get(i).getType()+ "| hp:"+allPokes.get(i).getHP()+ "| energy:"+allPokes.get(i).getEnergy()+RESET);
        }

        for(int i=0;i<4;) {  //letting user pick there 4 pokemons for battle
            Scanner kb = new Scanner(System.in);
            System.out.println("Enter Pokemon Number:");
            int pokeNum = Integer.parseInt(kb.nextLine()); //getting user input
            if (pokeNum <= 27 && pokeNum >= 0 && !(userPokes.contains(allPokes.get(pokeNum)))) { //if input is invalid code asks user again for there input
                userPokes.add(allPokes.get(pokeNum));
                i++;
            }
            else {
                System.out.println("invalid Pokemon");
            }
        }

        for(Pokemon n: userPokes){ //removes all picked pokemon from allpokes
            allPokes.remove(n);
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int startingTurn(){ //dictates who goes first
        int rand = (int)(Math.random()*2);
        return rand;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int randint(int low,int high){ //random number function(given a range)
        return (int)(Math.random()*(high-low+1)+(low)); //inclusive of high
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void printDivider() { //prints a divider
        System.out.println("================================================");
    }

    public static void welcomeMessage(){ //givs the user a nice greeting to the game

        try{
            Scanner inFile = new Scanner((new BufferedReader(new FileReader("junk.txt")))); //reads a file containing a ASCII pokemon title
            while (inFile.hasNextLine()){ //printing ASCII stuff
                System.out.println(CYAN+inFile.nextLine()+RESET);
            }
        }

        catch(IOException ex){
            System.out.println("where is junk text");
        }
        System.out.print("\n");
        System.out.print(BLUE+"Hello Trainer! Welcome to the Pulse Pokemon Arena! \n"+
                "Your mission is to bring your team of 4 Pokemon and face off against\n"+  //greeting message
                "a MASSIVE lineup of enemy Pokemon. Will you become Trainer Supreme?\n"+
                "or will you just be another loser...? Good Luck! \n"+RESET);
        System.out.println("Press Enter to Continue");
        Scanner kb = new Scanner (System.in);
        String nothing = kb.nextLine();
        printDivider();
    }
/////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void gameLoop() { //where the the ehole game takes place

        while (userPokes.size() > 0 && allPokes.size() > 0) { //game will be played as long as the user has at least one pokemon alive and the computer has one pokemon alive
            System.out.println(YELLOW+"-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-= ROUND "+battlecounter+" -=-=-=-=--==---=-=-=-=-=-==--=-=-=-=-==--=-=-=-=-==-=-=-"+RESET);
            battle(); //battle function(where the fighting takes place)
            battlecounter++;
        }
        if(allPokes.size()==0) { //victory
            System.out.println("yay, You defeated all the pokemon");
        }
        if(userPokes.size()==0){ //loss
            System.out.println("Better luck next time :(");
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int pickingPoke(){ //picking what action to perform(attack,retreat and pass)

        int option = choice(); //prints what choices they have available

        if(option==ATTACK) {  //attack
             return ATTACK;

         }

         if(option==RETREAT){ //retreat
             return RETREAT;

         }

         if(option==PASS){//pass
             return PASS;
         }

         return 3;
    }
/////////////////////////////////////////////////////////////////////
    public static void pickingPokeforBattle(){ //picking pokemon for the battle

        if(curUser != null && curUser.isDead()){ //if pokemon is dead
            System.out.println("Your current pokemon got K.o");
        }
        Scanner kb = new Scanner(System.in);
        neatUserPokesPrint(); //prints all
        System.out.println("pick your pokemon");
        int pickedIndex = kb.nextInt();
        curUser = userPokes.get(pickedIndex); //setting current user poke to chosen pokemon
        System.out.println(curUser.getName() + " I choose you");

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void battle(){ //this functoin is where all the fighting happens
        sethealthforuserpokes(); //adds 20 health to pokemons if possible
        resetDisabled(); //resets all the disabled on User pokemon
        int allPokesSize = allPokes.size();
        int randComputerPokemon = (int)(Math.random()*allPokesSize); //getting a random index for a computer pokemon
        int Turn = startingTurn(); //dictates who goes first for the round


        if(Turn==USER){System.out.println("user is going first");}
        else{System.out.println("computer is going first");}

        curComputer = allPokes.get(randComputerPokemon);//current computer pokemon for battle

        System.out.println("pick your pokemon your opponent is "+curComputer.getName());

        pickingPokeforBattle(); //user pickung what pokemon will be used in the battle

        while(userPokes.size()>0 && curComputer.isAlive()){
            /*if(curUser.isDead()){
                pickingPokeforBattle();
            }*/

            if(Turn==USER) {
                int d = pickingPoke(); //picking what your gonna do(attack,retreat and pass)

                if (curUser.getStuned()) { //skips there tuns if stunned
                    System.out.println(curUser.getName() + ": Your lucky I can't attack this round I've been stunned");
                    curUser.setStunned(false);//resetting stunned flag
                }


                if (Turn == USER && d == ATTACK) { //if user chooses to attack
                    userTurn(curUser, curComputer); //Attack function
                }

                if (Turn == USER && d == RETREAT) { //if user picks retreat
                    Scanner kb = new Scanner(System.in);
                    System.out.println(curUser.getName() + ": You have Choose to Retreat, pick a new pokemon");
                    neatUserPokesPrint(); //let user pick no pokemon for battle
                    curUser = userPokes.get(kb.nextInt());
                }

                if (Turn == USER && d == PASS) { //if user picks pass
                    System.out.println(curUser.getName() + ": you have choose to pass, you will be given 10 Energy");
                    curUser.giveEnergy(10); //give the user 10 energy
                }
            }

            else{ //computer turn
                computerTurn(curComputer,curUser); //computer turn function
            }

            Turn = USER+COMPUTER-Turn; //changing of turn variable

            System.out.println(RED + "----------------------------------------------------"+ RESET);  //printing divider
            if(curComputer.isAlive()){System.out.println(curComputer.getName()+" current HP: "+curComputer.getHP());} //printing current computer health if alive
            if(curUser.isAlive()){System.out.println(curUser.getName()+" current HP: "+curUser.getHP());} //printing current User health if alive

            if(curUser.isDead()){
                System.out.println(curUser.getName()+" is K.O");
                userPokes.remove((curUser));
            }

            rechargeAll(); //gives 10 energy at the end of the round

            System.out.println(PURPLE+"/////////////////////////////////////////////////////////////////////////////"+RESET);//divider


        }

        if(curComputer.isDead()) { //if user defeats current computer
            System.out.println(curComputer.getName() + " is K.O");
            allPokes.remove(curComputer);

        }

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void userTurn(Pokemon good, Pokemon bad) {  //only for attacking
        Scanner kb = new Scanner(System.in);
        ArrayList<Attack> att = curUser.validAttacks(); //contains all valid attacks available

        System.out.println("Your valid attacks are: ");
        for (int i = 0; i < att.size(); i++) {
            System.out.println(i+"."+att.get(i).getNameCost()); //prints all available attacks and related information
            }

            int index = kb.nextInt();
            good.attack(bad,att.get(index)); //attacking with attack function in pokemon class



    }
//////////////////////////////////////////////////////////////////////////////////
    public static void computerTurn(Pokemon good,Pokemon bad){  //computers turn
        ArrayList<Attack> att = curComputer.validAttacks(); //valid attacks
        int index = (int)(Math.random()*att.size());//index for random attack

        if(att.size()==0){ //if they have no energy
            System.out.println(curComputer.getName()+": Your lucky I'm out of energy I have to pass");
        }
        else if(bad.getStuned()) { //if stunned turn gets skipped
            System.out.println(curComputer.getName()+": I have been stunned");
            bad.setStunned(false); //reset flag
        }

        else {
            good.attack(bad,att.get(index)); //attacking with attack function in pokemon class
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void healAll(){
        curUser.heal();
        curComputer.heal();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int choice(){ //picking what action the user wants to perform
        int c;
        String[] moves = {"Attack","Retreat","pass"};
        ArrayList<Attack> att = curUser.validAttacks(); //valid attacks available

        if(att.size()==0){ //if no attacks are available
            Scanner kb = new Scanner(System.in);
            System.out.println("What's your move? Your choices are:");
            for(int i=1;i<3;i++){
                System.out.println(i+"."+moves[i]); //only displays retreat and pass
            }
            c = kb.nextInt();

        }

        else{ //attacks are available displays all options
            Scanner kb = new Scanner(System.in);
            System.out.println("What's your move? Your choices are:");
            for(int i=0;i<3;i++){
                System.out.println(i+"."+moves[i]);
            }
            c = kb.nextInt();

        }

        if(c==0){return ATTACK;}
        if(c==1){return RETREAT;} //returns what ever the user picked
        if(c==2){return PASS;}

        return 999;


    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void neatUserPokesPrint(){ //prints pokes in userPokes
        int c = 0;
        for(Pokemon n:userPokes){
            System.out.println(c+"."+n.getName()+" | TYPE:"+n.getType()+" | HP:"+n.getHP()+" | ENERGY:"+n.getEnergy());
            c++;
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void resetDisabled(){ //resets disabled flag
        for(Pokemon n: userPokes){
            n.setBeendisabled(false);
        }

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void rechargeAll(){ //recharges all pokemon in userpokes gives them 10 energy if possible
        for(int i=0; i<userPokes.size();i++){
            userPokes.get(i).giveEnergy(10);
        }
        curComputer.giveEnergy(10);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void sethealthforuserpokes(){//this method is used for giving the userpokes 20 health if possible
        for(Pokemon n: userPokes){
            n.regen(20);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

