package com.example.warehouseproject;

public class Product {
    public String product;
    public String company;
    public int price;
    public int weight;
    public int stock;
    public String warehouseId;

    public Product(String product, String company, int price, int weight, int stock, String warehouseId) {
        this.product = product;
        this.company = company;
        this.price = price;
        this.weight = weight;
        this.stock = stock;
        this.warehouseId = warehouseId;
    }

    public Product() {
    }
}
