package com.example.cse234termproject.Model;

public class FavoritesModel {
    String favouritesName;
    String favouritesImg;
    String favouritesDescription;
    int favouritesPrice;
    String documentId;


    public FavoritesModel() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public FavoritesModel(String favouritesName, String favouritesImg, String favouritesDescription, int favouritesPrice) {
        this.favouritesName = favouritesName;
        this.favouritesImg = favouritesImg;
        this.favouritesDescription = favouritesDescription;
        this.favouritesPrice = favouritesPrice;
    }

    public String getFavouritesName() {
        return favouritesName;
    }

    public void setFavouritesName(String favouritesName) {
        this.favouritesName = favouritesName;
    }

    public String getFavouritesImg() {
        return favouritesImg;
    }

    public void setFavouritesImg(String favouritesImg) {
        this.favouritesImg = favouritesImg;
    }

    public String getFavouritesDescription() {
        return favouritesDescription;
    }

    public void setFavouritesDescription(String favouritesDescription) {
        this.favouritesDescription = favouritesDescription;
    }

    public int getFavouritesPrice() {
        return favouritesPrice;
    }

    public void setFavouritesPrice(int favouritesPrice) {
        this.favouritesPrice = favouritesPrice;
    }
}
