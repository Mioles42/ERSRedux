package com.miolean.ers;/*
 * Miles Krusniak
 * ERSGame.java (ERS)
 * 
 * ERSGame is the work-horse of ERS. It's playGame() method (which does plenty more than play the game)
 * deals, directly or indirectly, with every other class in the program (except Main).
 * 
 * In order to split up the work that ERSGame must do, I have created some side-classes - informally called ERSClasses.
 * 
 */
import java.util.*;
import java.io.*;

class ERSGame {
    //ERSClasses - these are assigned by the constructor.
    ERSWindow window;
    ERSInput input;
    ERSAction doAction;
    ERSOption doOption;
    ERSConfig fileInput;
    
    //Control and config variables
    final int NUMBER_OF_CONTROLS = 3; 
    int numberOfPlayers = 2;
    int maxPlayers;
    ControlSet[] controls;
    char quit;
    boolean cheatsEnabled = true; //unimplemented feature
    
    //Deck variables
    Deck[] hands;
    Deck pile;
    
    //Flow and upkeep variables
    boolean switchTurn = false;
    int triesLeft = -1;
    
    public ERSGame() {
        //Creates an ERSGame with instance variables of all of the other ERSClasses.
        //Used by Main to start the game
        this.window = new ERSWindow();
        this.input = new ERSInput(this);
        this.doAction = new ERSAction(this);
        this.doOption = new ERSOption(this);
        this.fileInput = new ERSConfig();
    }
    
    public void createHands() {
        //Sets up hands (based on numberOfPlayers) and deals them each cards.
        //Used by ERSOption
        
        hands = new Deck[numberOfPlayers];
        Deck deck = new Deck();
        deck.shuffle(); //shuffle!
        pile = new Deck(52);
        
        for(int i = 0; i < numberOfPlayers; i++)  hands[i] = new Deck(52);
        
        int currentHand = 0;
        while(!(deck.isEmpty())) {
            deck.giveCard(hands[currentHand]);
            deck.setCard(null, 51);
            
            currentHand++;
            currentHand %= numberOfPlayers;
        }
    }
    
    public void configurate() {
        //Gets the raw config file data and converts it into a more usable form.
        //Uses ControlSet to hold all of the controls together.
        //Used by ERSOption
        
        //Variables
        char action; //The choice of the player
        char[] config = new char[1]; //Represents the raw config data. Initialization is worthless in this case but necessary to compile
        maxPlayers = 0; //The number of players specified by the config file.
        
        //Try to find and get the configuration.
        try {
            config = fileInput.parseConfig(ERSGame.class.getResource("config.txt").openStream());
        } catch (IOException e) { //If it doesn't work...
            //Set window to display the error
            window.setXY(500, 200);
            window.print("--Error--", "Config file not found or has repeats.", "a - quit and try to open config   any other key - quit");
            action = input.getInput();

            //If the user decides to try and find the config (in the case that it has repeats)
            if (action == 'a') {
                try { //Attempt to open the config
                    fileInput.openConfig(new File("config.txt"));
                } catch (FileNotFoundException f) {
                } //File is still not found
                catch (IOException g) {
                } //No idea what this one means... but I put it in there to satisfy Java's catch-or-specify requirement
            }
            //End program and window
            window.dispose();
            System.exit(0);
        }

        //Assuming everything worked (if it didn't, program would be closed), initialize a ControlSet array.
        controls = new ControlSet[52]; //52 is the hard maximum limit of players
        for(int i = 0; i < controls.length; i++) controls[i] = new ControlSet();
        
        quit = config[0]; //Quit is first in the config file
        for(int i = 0; config[i] != '\u0000'; i++) {
            if(i % NUMBER_OF_CONTROLS == 0) {
                //first comes play control. Set ControlSet player variable as well (though it is unused)
                controls[(i) / NUMBER_OF_CONTROLS].play = config[i + 1];
                controls[(i) / NUMBER_OF_CONTROLS].player = i / NUMBER_OF_CONTROLS + 1;
            }
            if(i % NUMBER_OF_CONTROLS == 1) controls[(i) / NUMBER_OF_CONTROLS].slap = config[i + 1]; //next in the line is the slap control
            if(i % NUMBER_OF_CONTROLS == 2) {
                //Every third control (plus one because of quit control) will be a count cards control
                //If we have reached this point we have completed one ControlSet, so add one to maxPlayers
                controls[(i) / NUMBER_OF_CONTROLS].count = config[i + 1];
                maxPlayers++;
            }
        }
    }
        
    public int tries(Deck pile) {
        //Uses an array to determine how many tries a given card on top of the pile will give. (face cards)
        //Used by ERSAction
        int[] faceCards = {-1,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,2,3,-1}; //0 and 14 are unused in actual play
        if(pile.cardAt(0) == null) return -1;
        return faceCards[pile.cardAt(0).rank];
    }
    
    private int randomPlayer() {
        //Unused, possible usage later. Picks a random player out of numberOfPlayers players.
        Random generator = new Random();
        return generator.nextInt(numberOfPlayers)+1;
    }
    
    private String buttonCode(char button) {
        //Given a button pressed by the player (action), generate a code based on the controls list.
        //The code contains two characters - one for player whose button that is, and one for what it does.
        String result = "";
        
        for(int i = 0; i < numberOfPlayers; i++) {
            if(controls[i].play == button || controls[i].slap == button || controls[i].count == button) { //if this button is a control of player 'i'
                result += (i + 1); //first letter of code will be the player number
                if(controls[i].play == button) result += 'p'; //second letter of the code designates what move is is, p for play
                else if(controls[i].slap == button) result += 's'; //s for slap
                else if(controls[i].count == button) result += 'c'; //c for count
                return result;
            }
        }
        
        return "!!"; //If action was invalid anyway, return a default code
    }
    
    public void playGame() {
        //Starts the game based on the new ERSClasses supplied by the constuctor.
        //Input variables
        char menuOption; //choice of player at menu level (main screen)
        char action = '0'; //choice of a player at game level
        char playType = '0'; //A character corresponding to what kind of play "action" was
        int playOf = '0'; //Who played action, 1 to numberOfPlayers
        
        //Upkeep variables
        int turn = 1; //1 to numberOfPlayers
        int deadPlayers = 0; //Too late to change the name... number of players that have lost
        String triesToPrint = ""; //Blank if no tries, otherwise a formatted triesLeft
        boolean winCondition = true;
        
        configurate(); //Sets controls based on config file.
        window.print("","",""); //Makes absolutely sure the window is clear before starting
        while(true){
            if(winCondition) { //recovery from last game
                //Deck and window cleanup
                window.setCardsVisible(false);
                window.clearCards();
                createHands();
                
                //Variable resetting
                turn = 1;
                winCondition = false;
                switchTurn = false;
                triesToPrint = "";
                deadPlayers = 0;
            }
            
            //Set up window, print and ask for input
            window.setXY(window.fontSize * 30, window.fontSize * 20);
            window.middlePrint("----------| Welcome to ERSGame! |----------");
            window.lowerPrint("1 - Play  2 - Rules  3 - Controls  4 - Settings  0 - Quit");
            window.fieldPrint("Enter one:");
            menuOption =  input.getInput();
            
            configurate(); //Sets controls based on config file. Not in if(wincondition) because config file might change!
            window.print("","",""); //clear info before starting
            
            if(menuOption >= 'A' && menuOption <= 'Z') window.setBackground(Character.toLowerCase(menuOption)); //Change background on uppercase input
            if(menuOption == '2') doOption.rules(); //go to "Rules"
            if(menuOption == '3') doOption.controls(); //go to "Controls"
            if(menuOption == '4') doOption.settings(); //go to "Settings"
            if(menuOption == '0') break; //Quit
            
            //Start the actual game. First, things to do right before starting...
            if(menuOption == '1') { 
                window.print("", "Starting " + numberOfPlayers + "-player game.", "Good luck!");
                window.setCardsVisible(true);
                window.setXY(window.fontSize * 30, window.fontSize * 30);
            }
            //then let's go!
            while(menuOption == '1') {
                window.setList(pile); //update cards display to current pile
                window.fieldPrint("P" + turn + triesToPrint + ":");
                
                action = input.getInput(); //get raw input
                playOf = buttonCode(action).charAt(0) - '0'; //first letter of code
                playType = buttonCode(action).charAt(1); //second letter of code
                
                window.print("","","");
                
                //Completely skips everything under certain circumstances
                if((playOf != turn && playType == 'p') || playOf == '!') continue;
                
                //Given the button pressed, who it belongs to (if anyone) and what it does (if anything)... see if it fits an action.
                //Non-play controls, does not matter whether play and hand are valid
                if(action == quit) {
                    winCondition = true; //Nobody won... but it is necessary to reset the game
                    break;
                }
                if(action >= 'A' && action <= 'Z') window.setBackground(Character.toLowerCase(action));
                //Play controls. Play must be a real play (not '!' as returned by buttonCode if play was not valid) and the player's hand must not be dead.
                if(playType != '!' && hands[playOf - 1].cardAt(0).rank != 0) { //if play was valid and hand was valid
                    if(playType == 'p' && playOf == turn) doAction.playCard(playOf);
                    if(playType == 's') doAction.slap(playOf); //Needn't be your turn to slap
                    if(playType == 'c') doAction.countCards(playOf); //or count cards
                }
                
                //Deals with tries remaining. "common time" = face card has not been played on the pile since the last pile-taking = "tries == -1"
                if(switchTurn) { //assuming a play was made...
                    if(tries(pile) == -1) { //if the play was a fluff card
                        if(triesLeft != -1) { //if the game is not in common time...
                            switchTurn = false;
                            triesLeft--;
                        } else switchTurn = true;
                    } else { //if the play was a face card...
                        triesLeft = tries(pile);
                        switchTurn = true;
                    }
                }
                
                //Deals with getting the pile when the person after you is out of tries. Here I deal with Player 1 running out of tries
                //separately since the logical player before Player 1 does not exist! (Player 1 - 1 = Player 0)
                if(triesLeft == 0){
                    if(turn == 1) { //since we do not want to send "0" to the getPile() method, and 1 will do that
                        window.print("", "Player " + turn + " is out of tries.", "Player " + (numberOfPlayers) + " gets the pile of " + pile.numberOfCards() + " cards!");
                        doAction.getPile(numberOfPlayers); //assert: numberOfPlayers is always the last player, the one before Player 1.
                    } else {
                        window.print("", "Player " + turn + " is out of tries.", "Player " + (turn - 1) + " gets the pile of " + pile.numberOfCards() + " cards!");
                        doAction.getPile(turn - 1);
                    }
                    triesLeft = -1; //Put the game back into common time //do not print tries
                }
                
                //Deals with under what conditions the tries should be printed
                if(triesLeft > 0) triesToPrint = " (" + triesLeft + " tries left)"; //print tries when ready
                else triesToPrint = "";
                
                //Chacks for any players tha have hands with a null card at the top (empty hand).
                //If so, assign the top Card a setinel value - rank of 0. This prevents us from saying "Player n is out of the game!" ad nauseum.
                //since 0 and null are two different things.
                for(int i = 0; i < hands.length; i++) {
                    if(hands[i].cardAt(0) == null) {
                        window.print("","","Player " + (i+1) + " is out of the game!");
                        hands[i].setCard(new Card(0,0), 0); //set to setinel value
                        deadPlayers++; //one more player with a dead hand...
                    }
                }
                if(deadPlayers + 1 == numberOfPlayers) winCondition = true; //If there is only one remaining player... game is over
                
                
                //Switches the turn. Also, if the win condition is true, finds the winning player.
                while(switchTurn || winCondition) {
                    turn %= numberOfPlayers; //limits range: 1 to numberOfPlayers
                    turn++;
                    
                    if(hands[turn - 1].cardAt(0).rank == 0) switchTurn = true; //if the active player's hand is dead, keep switching
                    else {
                        switchTurn = false;
                        break; //in case winCondition is true.
                    }
                }
                if(winCondition) { //If the game is over...
                    //The winning player will have the turn - all other players will be invalid
                    window.print("----==| " + turn + " |==----", "Player " + turn + " is the winner!", "Press any key to return to the menu.");
                    window.fieldPrint("");
                    input.getInput();
                    break;
                }
            }
        }
        window.dispose(); //If the true loop exits, kill the window before exiting the program
    }
}            
      