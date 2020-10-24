package com.jokers.todolist.models;

/**
 * Tags on Task
 *
 * Purpose: filter by tag
 * @author Anh Pham
 */
public class Tag {
    private String id;
    private String name;

    public Tag() {
    }

    public Tag(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
