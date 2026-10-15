package com.example.myapplicationth;

public class Task {
    private int id;
    private String title;
    private String time;
    private String note;
    private boolean isToday;
    private boolean isCompleted;
    private String username;

    public Task() {
    }

    public Task(int id, String title, String time, String note, boolean isToday, boolean isCompleted, String username) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.note = note;
        this.isToday = isToday;
        this.isCompleted = isCompleted;
        this.username = username;
    }

    public Task(String title, String time, String note, boolean isToday, boolean isCompleted, String username) {
        this.title = title;
        this.time = time;
        this.note = note;
        this.isToday = isToday;
        this.isCompleted = isCompleted;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
