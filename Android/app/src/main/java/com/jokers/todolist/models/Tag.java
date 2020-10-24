package com.jokers.todolist.models;

/**
 * Tags on Task
 *
 * Purpose: filter by tag
 * @author Anh Pham
 */
public class Tag {
    private String id;
    private String text;

    public Tag() {
    }

    public Tag(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void getText(String name) {
        this.text = text;
    }
}
