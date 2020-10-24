package com.jokers.todolist.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Task class
 * Purpose: Task class initialized by user class;
 *
 * @author Guan Li
 */

public class Task {
    //User's two attribute:
    private String id;
    private String title;
    private String description;
    private int createdDate;
    private int dueDate;
    private List<Tag> tags = null;

    //Constructor:
    Task() {
        tags = new ArrayList<>();
    }

    Task(String id, String title, String description, int createdDate, int dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    //Getters:
    public int getTimeRemain() {
        return this.dueDate - this.createdDate;
    }

    public String getID() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCreatedDate() {
        return this.createdDate;
    }

    public int getDueDate() {
        return this.dueDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    //Setters:
    public void setID(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedDate(int createdDate) {
        this.createdDate = createdDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Set tag to the tag list
     * @param position  Set tag at position
     * @param id        Tag id
     * @param text      Tag text
     */
    public void setTagAt(int position, String id, String text) {
        Tag tag = new Tag(id, text);
        tags.set(position, tag);
    }
}
