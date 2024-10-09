package com.example.berwerbungstrackingapp;

import java.io.Serializable;

public class Note implements Serializable {

    private int id;
    private String title;
    private String date;
    private String status;
    private String place;
    private String establishment;
    private String type;
    private String remarks;

    // Constructors

    public Note() {
        // Default constructor
    }

    public Note(String title, String date, String status, String place, String establishment, String type, String remarks) {
        this.title = title;
        this.date = date;
        this.status = status;
        this.place = place;
        this.establishment = establishment;
        this.type = type;
        this.remarks = remarks;
    }

    // Getter and Setter methods

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
