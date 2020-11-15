package com.jokers.todolist.models;

/**
 * Point Class:
 * Purpose: give each user points to access to the shop for customization of GUI
 * @author Guan Li
 */


public class Points{
    /**
     * Notes:
     * Two Attribute
     * Points
     * Maybe make this special to add to shop.
     * Furthur development on this class.
     */

    private int point;

    //Default constructor:
    public Points(){
        point = 0;
    }

    public Points(int point){
        this.point = point;
    }

    //access the user class

    public void addPoints(){
        point = point + 1;
    }


}