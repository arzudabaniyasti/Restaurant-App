package com.example.cse234termproject.Model;

import java.io.Serializable;

public class ReservationModel implements Serializable {
    String Date, Time, PersonNumber;
    String documentId;

    public  ReservationModel(){
    }
    public  ReservationModel(String date, String time, String peopleNumber) {
        this.Date = date;
        this.Time = time;
        this.PersonNumber = peopleNumber;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getPersonNumber() {
        return PersonNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.PersonNumber = personNumber;
    }
}
