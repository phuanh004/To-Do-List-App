package com.jokers.todolist.models;
import java.util.ArrayList;

/** 
 * Task class
 * Purpose: Task class initialized by user class;
 * @author Guan Li
*/

public class Task {
    //User's two attribute:
    private String id;
    private String title;
    private String description;
    private int createdDate;
    private int dueDate;
    private ArrayList<Tags> tags = new ArrayList<Tags>();

    //Constructor:
    Task(String id, String title, String description, int createdDate, int dueDate){ //Constructor:
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }
    public int getTimeRemain(){return this.dueDate -  this.createdDate;}
    //getter and setter
    // private String id;
    // private String title;
    // private String description;
    // private int createdDate;
    // private int dueDate;
    // private ArrayList<Tags> tags = new ArrayList<Tags>();
    public String getID(){return this.id;}
    public String getTitle(){return this.title;}
    public String getDescription(){return this.description;}
    public int getCreatedDate(){return this.createdDate;}
    public int getDueDate(){return this.dueDate;}
    
    public Tags[] getTags(){
        Tags[] temp = new Tags[tags.size()];
        for (int i = 0; i < tags.size(); i++){
            temp[i] = tags.get(i);
        }
        return temp;
    }
    //Setters:
    public void setID(String id){this.id = id;}
    public void setTitle(String title){this.title = title;}
    public void setDescription(String description){this.description = description;}
    public void setCreatedDate(int createdDate){this.createdDate = createdDate;}
    public void setDueDate(int dueDate){this.dueDate = dueDate;}
    public void setTags(Tags tag, int position){
        for (int i = 0; i < tags.size(); i++ ){if (i == position){tags.set(i,tags);}}}
}
