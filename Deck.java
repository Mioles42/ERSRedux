package com.miolean.ers;/*
 * Miles Krusniak
 * Deck.java (ERS)
 * 
 * Deck is another object-class, but this time used mainly for its behaviors.
 * It stores cards but also can interact with other Decks in certain ways.
 */

import java.util.Random; //used in shuffle()

class Deck {
    private Card[] contents;
    
    public Deck(int n) {
        //Constructor for a deck containing an amount of cards n, but with no known values yet
        
        contents = new Card[n];
    }
    
    public Deck() {
        //Default constructor, makes a standard 52 card deck.
        //The cards will be ordered by suit then rank.
        
        contents = new Card[52];
        int index = 0;
        
        for(int currentSuit = 0; currentSuit <= 3; currentSuit++){ //for each suit...
            for(int currentRank = 1; currentRank <= 13; currentRank++){ //for each rank...
                contents[index] = new Card (currentSuit, currentRank); //add a card
                index++;
            }
        }
    }
    
    public String toString() {
        //Returns the Deck in string form.
        //Not used, only for debugging.
        
        String result = "";
        int newLine = 0;
        
        for(int index = 0; contents[index] != null; index++){ //for each Card
            result += contents[index].toString();
            newLine++;
            //if the card is not the last one, add a comma...
            if (index != contents.length - 1) result += ",   ";
            if(newLine % 4 == 0){ //make a new line every 4 Cards
                result += "\n";
            }
        }
        return result;
    }
    
    //----------
    //
    //All methods beyond this point are unique to Decks and actually used within the game.
    //
    //----------
    
    public void shuffle() {
        //Puts the cards in a Deck into a random order.
        
        Card currentCard; //temp value
        Random random = new Random();
        int currentRandom = 0;
        
        //Loop: For every card, switch it with a random other card.
        for(int index = 0; index < contents.length; index++) {
            currentRandom = random.nextInt(contents.length);
            currentCard = contents[index];
            contents[index] = contents[currentRandom];
            contents[currentRandom] = currentCard;
        }
    }
    
    public void addSpaceFront() {
        //Moves all  the cards in a Deck up one, so that the Card at contents[0] is a duplicate
        //of the Card at [1] and can be overwritten.
        
        int i;
        for(i = contents.length - 1; i > 0; i--) contents[i] = contents[i-1];
        contents[0] = null; //just in case, I don't want a duplicate card on the loose
    }
    
    public void removeSpaceFront() {
        //Moves all the cards in a Deck back one, so the Card at contents[0] will be deleted
        
        for(int i = 0; i < contents.length - 1; i++) contents[i] = contents[i+1];
    }
    
    public void giveCard(Deck deck) {
        //Moves a card from the front of this Deck instance to the front of another Deck.
        
        deck.addSpaceFront();
        deck.contents[0] = this.contents[0];
        this.removeSpaceFront();
    }
    
    public void giveAll(Deck deck) {
        ///Moves all of the cards from this Deck to the BACK of another Deck.
        
        int i;
        for(i = 0; deck.contents[i] != null && i < 52; i++); //finds end of the other Deck
        
        //Once we know where the null Cards in the other deck start, we can start overwriting
        //them with this Deck's cards.
        while(this.contents[0] != null) {
            deck.contents[i] = this.contents[0];
            this.removeSpaceFront();
            i++;
        }
    }
    
    //The following methods deal with counting cards
    public int numberOfCards() {
        //Since contents is private, this method returns how many cards there are in contents
        //that aren't null.
        
        int result = 0;
        for(int i = 0; i < contents.length; i++){
            if(contents[i] != null) result++;
        }
        return result;
    }
    public boolean isEmpty() {
        //Quick way to tell if a Deck is empty. Not really necessary but it's here anyway.
        return numberOfCards() == 0;
    }
    
    //The following methods deal with accessing contents.
    //I don't use them within this class, even when dealing with separate instances of Deck,
    //because contents can be dealt with from here. (it would be redundant)
    public Card cardAt(int index) {
        //Since contents is private, returns the card at the specified index of contents.
        return contents[index];
    }
    public void setCard(Card card, int index) {
        //Since contents is private, changes the card at the specified index of contents.
        //to another card.
        contents[index] = card;
    }
}