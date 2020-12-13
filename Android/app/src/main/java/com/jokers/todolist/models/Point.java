package com.jokers.todolist.models;

/**
 * Point Class:
 * Purpose: give each user points to access to the shop for customization of GUI
 *
 * @author Guan Li
 */

public class Point {
    /**
     * Notes:
     * Two Attribute
     * Points
     * Maybe make this special to add to shop.
     * Furthur development on this class.
     */

    private int totalPoints;

    //Default constructor:
    public Point() {
    }

    public void addPoints(){
        totalPoints += 1;
    }

}