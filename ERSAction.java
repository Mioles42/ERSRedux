package com.miolean.ers;/*
 * Miles Krusniak
 * ERSAction.java (ERS)
 * 
 * ERSAction deals with all of the actions a player may do. There are three - slap, play, and count - but
 * they also have other methods required to make them work.
 */
import java.io.*;

public class ERSAction {
    
    //SlapType is an easy way to refer to different kinds of slaps, ERSGame is the executing game
    private enum SlapType {badSlap, tenOfDiamondsSlap, sandwichSlap, doubleSlap, emptySlap}
    ERSGame game;
    
    public ERSAction(ERSGame game) {
        this.game = game;
    }
    //----------
    //Slapping methods
    //----------
    public void slap(int player) {
        //Simulates the player "slapping" the pile
        Deck pile = game.pile;
        ERSWindow window = game.window;
        SlapType slapType = isSlap(pile);
        
        if(slapType == SlapType.sandwichSlap) { //if slap is a sandwich
            window.print("***SLAP by Player " + player + "!***", "Slap successful! (Sandwich)", "Player " + player + " gains the pile of " + pile.numberOfCards() + " cards!");
            getPile(player);
            game.triesLeft = -1;
        }else if(slapType == SlapType.doubleSlap) { //if slap is a double
            window.print("***SLAP by Player " + player + "!***", "Slap successful! (Double)", "Player " + player + " gains the pile of " + pile.numberOfCards() + " cards!");
            getPile(player);     
            game.triesLeft = -1;
        }else if(slapType == SlapType.tenOfDiamondsSlap) { //if slap is a ten of diamonds
            window.print("***SLAP by Player " + player + "!***", "Slap successful! (10 of Diamonds)", "Player " + player + " gains the pile of " + pile.numberOfCards() + " cards!");
            getPile(player);
            game.triesLeft = -1;
        }else if(slapType == SlapType.badSlap) { //if slap is not a slap at all
            window.print("***SLAP by Player " + player + "!***", "Slap unsuccessful.", "Player " + player + " burns a " + burnCard(player) + "!");
        }else if(slapType == SlapType.emptySlap) { //if a player slaps when there is no pile (very common!)
            window.lowerPrint("Empty pile.");
        }
        
        game.switchTurn = false; //slapping does not require the turn
    }
    private SlapType isSlap(Deck pile) {
        //Tests what kind of slap, if any, is in the pile.
        if(pile.numberOfCards() == 0) return SlapType.emptySlap; //Empty pile
        if(pile.cardAt(2) != null && pile.cardAt(0).rank == pile.cardAt(2).rank) return SlapType.sandwichSlap; //Sandwich
        if(pile.cardAt(1) != null && pile.cardAt(0).rank == pile.cardAt(1).rank) return SlapType.doubleSlap;//Double
        if(pile.cardAt(0).rank == 10 && pile.cardAt(0).suit == 3) return SlapType.tenOfDiamondsSlap; //10 of Diamonds
        return SlapType.badSlap; //Not a slap
    }
    private Card burnCard(int player){
        //Burns a card from the deck of player to the bottom of the pile, and returns the burnt card.
        int i;
        Card burnt;
        Deck hand = game.hands[player - 1];
        
        //find end of pile
        for(i = 0; game.pile.cardAt(i) != null; i++);
        
        //Put the burnt card at the bottom (end) of the pile
        burnt = hand.cardAt(0);
        game.pile.setCard(hand.cardAt(0), i);
        hand.removeSpaceFront();
        
        return burnt;
    }
    //----------
    //Count method
    //----------
    public void countCards(int player) {
        //Displays the number of cards in a player's hand.
        game.window.print("", "", "P" + player + ", you have " + game.hands[player - 1].numberOfCards() + " cards left.");
        game.switchTurn = false;
    }
    //----------
    //Playing methods
    //----------
    public void playCard(int player) {
        //Gives the top card of a player's hand to the top of the pile.
        game.hands[player - 1].giveCard(game.pile);
        game.switchTurn = true; //Playing requires the turn
    }
    public void getPile(int player) {
        //Gives a player's hand all of the cards in the paile
        game.pile.giveAll(game.hands[player - 1]);
        game.switchTurn = true; //Switch turn from whoever lost on the tries
    }
}