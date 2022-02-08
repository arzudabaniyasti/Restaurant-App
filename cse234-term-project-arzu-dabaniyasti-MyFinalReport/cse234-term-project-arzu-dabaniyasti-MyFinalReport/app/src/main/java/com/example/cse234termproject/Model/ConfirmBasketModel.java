package com.example.cse234termproject.Model;

import java.io.Serializable;

public class ConfirmBasketModel implements Serializable {
    String CardName;
    boolean isSelected;
    String documentId;

    public ConfirmBasketModel() {
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getCardName() {
        return CardName;
    }

    public void setCardName(String CardName) {
        this.CardName = CardName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ConfirmBasketModel(String CardName) {
        this.CardName = CardName;
    }
}
