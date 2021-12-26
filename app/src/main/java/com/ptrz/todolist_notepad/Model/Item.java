package com.ptrz.todolist_notepad.Model;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String event, date;
    private Boolean important = false;
    private String isImportant = "";
    private String details = "";

    public Item(String event) {
        this.event = event;
        this.date = "";
    }

    public Item(String event, String date) {
        this.event = event;
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
        if (important) {
            this.isImportant = "\uD83D\uDD1D";
        } else {
            this.isImportant = "";
        }
    }

    public String getIsImportant() {
        return isImportant;
    }

    public static void reverse(List<Item> l) {
        List<Item> result = new ArrayList<>();
        for (int i = l.size() - 1; i >= 0; i--) {
            result.add(l.get(i));
        }
        for (int i = 0; i < l.size(); i++) {
            l.set(i, result.get(i));
        }
    }
}