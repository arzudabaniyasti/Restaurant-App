package com.example.cse234termproject.Model;

import java.io.Serializable;

public class OrderProductModel implements Serializable {
    String ProductName,quantity;
    String documentId;

    public String getProductName() {
        return ProductName;
    }
    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public OrderProductModel() {
    }
    public OrderProductModel(String ProductName, String quantity) {
        this.ProductName=ProductName;
        this.quantity=quantity;
    }

}