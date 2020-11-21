package com.jokers.todolist.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Task class
 * Purpose: Task class initialized by user class;
 *
 * @author Guan Li
 */

@IgnoreExtraProperties
public class ToDo implements Serializable {
    //User's two attribute:

    @Exclude
    private String id;          // PK: To-do id

    @Exclude
    private String uid;         // FK: User id

    @PropertyName("title")
    private String title;

    @PropertyName("description")
    private String description;

    @PropertyName("created_date")
    private String createdDate;

    @PropertyName("due_date")
    private String dueDate;

    @PropertyName("do_date")
    private String doDate;

    @Exclude
    private boolean expanded;                   // Use for the recyclerview

//    @PropertyName("tags")
    private List<Tag> tags = null;

    //Constructor:
    public ToDo() {
        tags = new ArrayList<>();
    }

    public ToDo(String id, String title, String description, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    //Getters:
    @Exclude
    public String getID() {
        return this.id;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @PropertyName("title")
    public String getTitle() {
        return this.title;
    }

    @PropertyName("description")
    public String getDescription() {
        return this.description;
    }

    @PropertyName("created_date")
    public String getCreatedDate() {
        return this.createdDate;
    }

    @PropertyName("due_date")
    public String getDueDate() {
        return this.dueDate;
    }

    @PropertyName("do_date")
    public String getDoDate() {
        return doDate;
    }

    /**
     * Calculate date remaining for due date
     * @return date remaining
     */
    @Exclude
    public Integer getRemainingDays() {
        if (dueDate != null){
            Date _dueDate = new Date(Long.parseLong(getDueDate()) * 1000L);

            LocalDate dueDate = new LocalDate(_dueDate);
            LocalDate currentDate = LocalDate.now();

            return Days.daysBetween(currentDate, dueDate).getDays();
        }

        return null;
    }

    /**
     * Format the remaining days
     * @return result in display format
     */
    @Exclude
    public String getFormattedRemainingDays() {
        String result = "";
        Integer remainDays = getRemainingDays();

        if (remainDays == null) { return result; }
        else if (remainDays == 0) {
            result = "Due today";
        } else if (remainDays > 0) {
            result = remainDays + " days left";
        } else if (remainDays < 0){
            result = Math.abs(remainDays) + " days ago";
        }

        return result;
    }

    @Exclude
    public String getCurrentDate(){
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    @Exclude
    public List<Tag> getTags() {
        return tags;
    }

    //Setters:

    @Exclude
    public void setID(String id) {
        this.id = id;
    }

    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("created_date")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @PropertyName("due_date")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @PropertyName("do_date")
    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }

    @Exclude
    public boolean isExpanded(){
        return this.expanded;
    }

    /**
     * All type
     *
     * @param format
     * type:
     *      "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
     *      "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
     *      "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
     *      "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
     *      "yyMMddHHmmssZ"-------------------- 010704120856-0700
     *      "K:mm a, z" ----------------------- 0:08 PM, PDT
     *      "h:mm a" -------------------------- 12:08 PM
     *      "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
     *
     * @return a String for display
     */
    @Exclude
    public String getDateInDisplayFormat(String format, String unixTime) {
       if (unixTime == null) {
           return "";
       }
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(Long.parseLong(unixTime) * 1000L);

        return df.format(date);
    }

    /**
     * Set tag to the tag list
     * @param position  Set tag at position
     * @param id        Tag id
     * @param text      Tag text
     */
    @Exclude
    public void setTagAt(int position, String id, String text) {
        Tag tag = new Tag(id, text);
        tags.set(position, tag);
    }

    @Exclude
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
