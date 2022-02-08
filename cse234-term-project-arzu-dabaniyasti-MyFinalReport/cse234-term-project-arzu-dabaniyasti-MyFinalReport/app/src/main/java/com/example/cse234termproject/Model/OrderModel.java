package com.example.cse234termproject.Model;

import java.io.Serializable;

public class OrderModel implements Serializable {
    String date,time,totalBasket;
    String documentId;


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public OrderModel() {
    }

    public OrderModel(String date, String time, String totalBasket) {
        this.date = date;
        this.time = time;
        this.totalBasket = totalBasket;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalBasket() {
        return totalBasket;
    }

    public void setTotalBasket(String totalBasket) {
        this.totalBasket = totalBasket;
    }
}