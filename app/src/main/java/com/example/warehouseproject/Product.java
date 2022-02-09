package com.example.warehouseproject;

import java.util.Map;

public class Product {
    public String id;
    public String product;
    public String company;
    public int price;
    public int weight;
    public int stock;
    public String warehouseId;

    public Product(String id, String product, String company, int price, int weight, int stock, String warehouseId) {
        this.id = id;
        this.product = product;
        this.company = company;
        this.price = price;
        this.weight = weight;
        this.stock = stock;
        this.warehouseId = warehouseId;
    }

    public Product() {
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
