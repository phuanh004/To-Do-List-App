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
        this.setPoints(0);
    }

    public Point(int points) {
        this.setPoints(points);
    }

    public int getPoints() {
        return totalPoints;
    }

    public void setPoints(int points) {
        this.totalPoints = points;
    }

    public void increasePoints() {
        totalPoints += 1;
    }

    public void decreasePoints() {
        totalPoints -= 1;
    }

}