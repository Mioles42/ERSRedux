package com.miolean.ers;/*
 * Miles Krusniak
 * Card.java (ERS)
 * 
 * Card is meant to be used as an object for its instance variables
 * and has no behaviors (except toString).
 */

class Card{
    
    //Instance variables are not private since that's pretty much all Card is made of
    int suit;
    int rank;
    
    public Card(){
        //Constructor for default values
        suit = 0;
        rank = 0;
    }
    public Card(int suit, int rank){
        //Constructor for specific values
        this.suit=suit;
        this.rank=rank;
    }
    public String toString(){
        //Puts a Card in String form. It will be in the form "[rank] of suit", or with curly brackets for face cards
        
        //Arrays representing formal names of suits and ranks
        String[] suitNames = {"Spades","Hearts","Clubs","Diamonds"};
        String[] rankNames = {"0Null","Ace","2","3","4","5","6","7","8","9","10","Jack","Queen","King","14Null"};
        
        //If the card is a face card, use curly brackets. If not but it is valid, use normal brackets.
        if(rank==1||rank==11||rank==12||rank==13) return ("{"+rankNames[rank]+"}" + " of " + suitNames[suit] + "\n");
        if(rank > 0 && rank < 14) return ("["+rankNames[rank]+"]" + " of " + suitNames[suit] + "\n");
        return "-----" + "\n"; //If the rank is invalid (used in previous versions as an indicator of an empty card) return default.
    }
}