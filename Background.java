package com.miolean.ers;/*
 * Miles Krusniak
 * Background.java (ERS)
 * 
 * Background specifies a number of color sets that, when setBackground is called, can be applied
 * to the given ERSWindow.
 */

import java.awt.Color;

public class Background {
    
    private static ERSWindow window; //window for changes to be applied to
    
    public static void setBackground(ERSWindow w, char input) {
        //Based on a given character that is the 'code' for that background, applies it to given ERSWindow.
        window = w;
        if(input == 'i') iceBackground();
        if(input == 'f') flameBackground();
        if(input == 's') slateBackground();
        if(input == 'p') plainBackground();
        if(input == 'j') jungleBackground();
    }
    //----------
    //
    //The following methods set the individual parts of each colored part in the window.
    //
    //----------
    private static void setAccent(Color color) {
         //"Accent" - text box, cards display whether or not it is visible
         
         window.field.setBackground(color);
         window.cards.setBackground(color);
         window.pileDisplay.setBackground(color);
    }
    private static void setMiddle(Color color) {
        //"Middle" - the panel with the text field
        
        window.mainPanel.setBackground(color);
        window.fieldLabel.setBackground(color);
    }
    private static void setTop(Color color) {
        //"Top" - the upper panels, where information is displayed
        
        window.infoPanel1.setBackground(color);
        window.infoPanel2.setBackground(color);
        window.infoPanel3.setBackground(color);
        window.info1.setBackground(color);
        window.info2.setBackground(color);
        window.info3.setBackground(color);
    }
    //----------
    //
    //The following methods are all background presets.
    //
    //----------
    private static void iceBackground() {
        //Light blue theme
        setTop(new Color(170, 200, 255));
        setMiddle(new Color(150, 170, 255));       
        setAccent(new Color(200, 220, 255));
        window.upperPrint("'ice' selected.");
    }
    private static void flameBackground() {
        //Red theme
        setTop(new Color(240, 210, 190));
        setMiddle(new Color(255, 150, 120));
        setAccent(new Color(200, 185, 180));
        window.upperPrint("'flame' selected.");
    }
    private static void slateBackground() {
        //Gray-green theme
        setTop(Color.lightGray);
        setMiddle(new Color(180, 185, 180));
        setAccent(new Color(200, 205, 200));
        window.upperPrint("'slate' selected.");
    }
    private static void plainBackground() {
        //Basic, default theme
        setTop(Color.white);
        setMiddle(new Color(200, 200, 200));
        setAccent(new Color(240, 240, 240));
        window.upperPrint("'plain' selected.");
    }
    private static void jungleBackground() {
        //Green theme
        setTop(new Color(135, 230, 130));
        setMiddle(new Color(20, 200, 20));
        setAccent(new Color(100, 220, 100));
        window.upperPrint("'jungle' selected.");
    }
}