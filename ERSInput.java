package com.miolean.ers;/*
 * Miles Krusniak
 * ERSInput.java (ERS)
 * 
 * ERSInput deals specifically with with the text box of ERSWindow, retrieving values from it when asked.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ERSInput {
    //Like most other ERSClasses takes an ERSGame as parameter
    ERSGame game;
    public ERSInput(ERSGame game) {
        this.game = game;
    }
    public char getInput () {
        //Gets the player's input from the text box.
        while(game.window.field.getText().equals("")); //wait for input to appear
        char result = game.window.field.getText().charAt(0);
        game.window.field.setText(""); //empty the text box
        return result;
    }
}