package com.example.cse234termproject.Model;


public class UserModel {

    public String name, surname, email, phone, birthdate, password;

    public UserModel(){
    }

    public UserModel(String name, String surname, String email, String phone, String birthdate, String password){
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.phone=phone;
        this.birthdate=birthdate;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}