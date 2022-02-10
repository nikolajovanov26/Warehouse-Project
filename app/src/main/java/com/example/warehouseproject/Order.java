package com.example.warehouseproject;

public class Order {
    public String id, storeId, warehouseId,status;
    public Integer prodNum;

    public Order() {
    }

    public Order(String id, String storeId, String warehouseId,Integer prodNum) {
        this.id = id;
        this.storeId = storeId;
        this.warehouseId = warehouseId;
        this.prodNum = prodNum;
        this.status = "Not confirmed";
    }
}
