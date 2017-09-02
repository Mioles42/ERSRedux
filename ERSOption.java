package com.miolean.ers;/*
 * Miles Krusniak
 * ERSOption.java (ERS)
 * 
 * ERSOption deals with all of the options at the main screen, making ERSGame.playGame() more readable.
 */
import java.io.*;

class ERSOption {
    ERSGame game; //The executing ERSGame
    
    public ERSOption(ERSGame game) {
        //Constructor
        this.game = game;
    }
    
    
    public void controls() {
        //Player chooses "Controls" at main screen
        
        //Set up some absolutely necessary variables, others will be referred to through 'game.variableName' Also set window size.
        ERSWindow window = game.window;
        ERSInput input = game.input;
        window.setXY(window.fontSize * 50, window.fontSize * 15);
        
        //Print:
        //Controls for each player, based on numberOfPlayers (NOT maxPlayers)
        for(int i = 0; i < game.numberOfPlayers; i++) {
            window.upperPrint("Player " + (i + 1) + " play: " + (char) game.controls[i].play);
            window.middlePrint("Player " + (i + 1) + " slap: " + (char) game.controls[i].slap);
            window.lowerPrint("Player " + (i + 1) + " count cards: " + (char) game.controls[i].count);
            window.fieldPrint("Any key to continue:");
            input.getInput();
            window.print("","","");
        }
        
        //Universal controls
        window.upperPrint("Quit within the game by entering '" + game.quit + "'.");
        window.middlePrint("You can change these controls by changing the config file.");
        window.lowerPrint("Change the number of players in 'settings'. The config file is set up for " + game.maxPlayers + " players.");
        window.fieldPrint("Any key to finish:");
        input.getInput();
        window.print("","","");
    }
    
    
    public void rules() {
        //Player chooses "Rules" at main screen
        
        //Set up some necessary variables. (and window size)
        ERSWindow window = game.window;
        ERSInput input = game.input;
        window.setXY(window.fontSize * 70, window.fontSize * 15);
        
        //Print rules of ERS, in "chunks"
        window.upperPrint("Each player is dealt some cards their hand. You can't see your hand.");
        window.middlePrint("Players take turns playing cards from their hands.");
        window.lowerPrint("A card played from a hand is displayed in the \"pile\" as the topmost card.");
        window.fieldPrint("Any key to continue:");
        input.getInput();
        window.print("","","");
        
        window.upperPrint("The goal is to get all of the cards.");
        window.middlePrint("There are two ways to get cards.");
        window.fieldPrint("Any key to continue:");
        input.getInput();
        window.print("","","");
        
        window.upperPrint("One: If the top card and the card one or two cards below it have the same rank,");
        window.middlePrint("you can \"Slap\" the pile to get the cards. The 10 of Diamonds may also always be slapped.");
        window.lowerPrint("But don't slap if it's not a slap! (go to 'Controls' for slapping and other controls)");
        window.fieldPrint("Any key to continue:");
        input.getInput();
        window.print("","","");
        
        window.upperPrint("Two: If you play a face card, your opponent has a certain amount of chances");
        window.middlePrint("to play another face card. If they don't, you get the pile.");
        window.lowerPrint("Ace: 4 chances  King: 3  Queen: 2  Jack: 1");
        window.fieldPrint("Any key to finish:");
        input.getInput();
        window.print("","","");
    }
    
    
    public void settings() {
        //Player chooses "Settings" at main screen
        
        //Set up some absolutely necessary variables, others will be referred to through 'game.variableName' Also set window size.
        ERSWindow window = game.window;
        ERSInput input = game.input;
        window.setSize(window.fontSize * 40, window.fontSize * 15);
        char action;
        
        //Options:
        window.print("","1 - Number of players   2 - Background","3 - Open config file   4 - Font");
        action = input.getInput();
        
        //Player chooses "Number of players"
        if(action == '1') {
            window.print("","","Enter number of players: ");
            game.numberOfPlayers = input.getInput() - '0';
            
            //While input is invalid, say so, keep asking for input
            while(game.numberOfPlayers > game.maxPlayers || game.numberOfPlayers < 2) {
                window.print("","The config file specifies " + (game.maxPlayers) + " players.", "Please use a number between that and 2 (inclusive)");
                game.numberOfPlayers = input.getInput() - '0';
            }
            
            window.print(game.numberOfPlayers + "-player mode selected.","",""); //Echo player's choice
            game.createHands(); //since we changed the number of hands needed
        }
        
        //Player chooses "Background"
        if(action == '2') {
            //Set a ackground based on input and Background class
            window.print("Enter a background: ","s - 'slate'   i - 'ice'   j - 'jungle'","f - 'flame'   p - 'plain'");
            action = input.getInput();
            Background.setBackground(window, action);
        }
        
        //Player chooses "Open config file"
        if(action == '3') {
            //Attempt to open the config file. 
            try {
                game.fileInput.openConfig(new File("config.txt"));
            } catch (FileNotFoundException e) {
                window.print("--Error--","Config file not found.", "Press any key to continue...");
                input.getInput();
            } catch (IOException e) {
                window.print("--Error--","Config file not found.", "Press any key to continue...");
                input.getInput();
            }
        }
        
        //Player chooses "Font"
        if(action == '4') {
            window.print("Press 'w' to increase size by 1 or 's' to decrease size by one","Press 'q' to finish resizing font", "Current size: " + window.fontSize);
            action = input.getInput();
            while(action != 'q') {
                if(action == 'w' && window.fontSize < 40) window.fontSize++; //hard upper limit 40
                if(action == 's' && window.fontSize > 1) window.fontSize--; //hard lower limit of 1
                window.lowerPrint("Current size: " + window.fontSize);
                
                window.resetFont();
                window.setSize(window.fontSize * 50, window.fontSize * 20);
                action = input.getInput();
            }
            
            window.print("Choose a font: ", "a - 'Arial'   c - 'Courier'", "s - 'Serif'");
            action = input.getInput();
            if(action == 'a') window.fontName = "Arial";
            if(action == 't') window.fontName = "Courier";
            if(action == 's') window.fontName = "Serif";
            window.resetFont();
            window.print(window.fontName + " size " + window.fontSize + " chosen.","","");
        }
    }
}