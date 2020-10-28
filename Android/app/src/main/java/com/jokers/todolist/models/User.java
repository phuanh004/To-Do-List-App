package com.jokers.todolist.models;

import java.util.ArrayList;
import java.util.List;

public class User{
    
    /**
     * User Class:
     * Purpose: define the user and user's access to tags and task
     * @author Guan Li
     */

    //method login, item, shopaccess, id

    String id; //Id for access
    String password; //password for access
    int level; //level for accessing to the shop or interaction with the shop.
    String email; //user's email address
    String avatar; // TODO: not really sure how this one work
    //initiate an object called task

    //methods
    
    /*
    public login(){

    }
    */
    ArrayList<Task> taskList = new ArrayList<Task>();

    public User(){
        System.out.println("A empty user created");
        this.id = "Guest";
        this.password = null;
        this.email = null;
        this.avatar = null;
        this.level = 0;
    }

    public User(String id, String password, int level, String email, String avatar){
        System.out.println("A user have been created");
        this.id = id;
        this.password = password;
        this.level = level;
        this.email = email;
        this.avatar = avatar;
    }

    //getter
    public String getId(){
        return this.id;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public int getLevel(){
        return this.level;
    }
    public Task getTask(int position){
        return taskList.get(position);
    }


    //setter:
    public void setId(String id){
        this.id = id;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmial(String email){
        this.email = email;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public void addTask(Task task){
        taskList.add(task);
    }


}