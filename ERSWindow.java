package com.miolean.ers;/*
 * Miles Krusniak
 * ERSWindow.java (ERS)
 * 
 * ERSWindow's purpose is to set up and organize a window for
 * ERSGame to display on.
 * 
 * Note: I have very little experience outside of command-line programs, so I experimented
 * a bit. I wound up using the combination of both java.swing's BoxLayout and java.awt's
 * components. Actually, it seems to work fairly well.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ERSWindow extends Frame implements WindowListener {
    
    //Components of the window. None are private - they are all used by Background.
    //Panels to contain other items
    Panel mainPanel = new Panel();
    Panel pileDisplay = new Panel();
    Panel infoPanel1 = new Panel();
    Panel infoPanel2 = new Panel();
    Panel infoPanel3 = new Panel();
    
    //Labels (plain text)
    Label info1 = new Label("");
    Label info2 = new Label("");
    Label info3 = new Label("");
    Label fieldLabel = new Label("");
    
    //Field (text entry) and cards display
    TextField field = new TextField(10);
    TextArea cards = new TextArea(10, 10);
    
    //Font information
    int fontSize = 12; //default 12
    String fontName = "Arial"; //default Arial
    int fontType = Font.PLAIN;
    
    public ERSWindow () {
        //Constructor - sets up the window in the orgamization that I want it in.
        
        //Basic layout, text field
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        mainPanel.add(fieldLabel);
        mainPanel.add(field);
        //Info
        infoPanel1.add(info1);
        infoPanel2.add(info2);
        infoPanel3.add(info3);
        add(infoPanel1);
        add(infoPanel2);
        add(infoPanel3);
        add(mainPanel);
        //Cards display
        add(cards);
        cards.setEditable(false);
        mainPanel.setSize(new Dimension(200,50));
        
        //Fonts
        Font ersFont = new Font("Arial", Font.PLAIN, fontSize);
        info1.setFont(ersFont);
        info2.setFont(ersFont);
        info3.setFont(ersFont);
        fieldLabel.setFont(ersFont);
        cards.setFont(ersFont);
        
        //General window settings
        setResizable(true);
        setVisible(true);
        setTitle("ERSGame");
        setSize(200, 300);
        addWindowListener(this);
        Background.setBackground(this, 'p'); //set to plain background
    }
    
    //----------
    //Methods from WindowListener. It appears that I must list them all even though I am only using one.
    //----------
    public void windowClosing(WindowEvent e) {
        //Close the window and program when close button is pushed.
        dispose();
        System.exit(0);
    }
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    //----------
    //Print methods. All of them display text in various locations.
    //----------
    public void upperPrint(String setTo) {
        //Display text in upper label.
        info1.setText(setTo);
        setVisible(true);
    }
    public void middlePrint(String setTo) {
        //Display text in middle label.
        info2.setText(setTo);
        setVisible(true);
    }
    public void lowerPrint(String setTo) {
        //Display text in lower label.
        info3.setText(setTo);
        setVisible(true);
    }
    public void fieldPrint(String setTo) {
        //Display text in label right before the text field.
        fieldLabel.setText(setTo);
        setVisible(true);
    }
    public void print(String setUpper, String setMid, String setLower) {
        //Display text in all of the labels except fieldLabel.
        info1.setText(setUpper);
        info2.setText(setMid);
        info3.setText(setLower);
        setVisible(true);
    }
    //----------
    //General display methods.
    //----------
    public void setList(Deck deck) {
        //Given a Deck (the pile), displays its cards on cardsDisplay
        clearCards();
        cards.replaceRange("", 0, 1000);
        for(int i = 0; deck.cardAt(i) != null; i++) {
            cards.insert(deck.cardAt(i).toString(), i * 50);
        }
    }
    public void clearCards() {
        //Replaces the Cards display with blankness.
        for(int i = 0; i < 54; i++) {
            cards.insert("\n", i);
        }
    }
    public void setXY(int x, int y) {
        //Sets the size of the ERSWindow.
        this.setSize(x,y);
    }
    public void setCardsVisible(boolean visible) {
        //Sets whether the card display is visible.
        if(visible) add(cards);
        else remove(cards);
    }
    public void setBackground(char input) {
        //Sets the background using class Background.
        Background.setBackground(this, input);
    }
    public void resetFont() {
        Font ersFont = new Font(fontName, fontType, fontSize); //create a new font
        
        info1.setFont(ersFont);
        info2.setFont(ersFont);
        info3.setFont(ersFont);
        fieldLabel.setFont(ersFont);
        cards.setFont(ersFont);
        
        setVisible(true);
    }
}