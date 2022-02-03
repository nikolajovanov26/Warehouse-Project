package com.example.warehouseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewOrders extends AppCompatActivity implements View.OnClickListener {

    Button main, products, orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        products = findViewById(R.id.products);
        products.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main:
                startActivity(new Intent(this, MainWarehouse.class));
                break;
            case R.id.products:
                startActivity(new Intent(this, AddProducts.class));
                break;
            case R.id.orders:
                //startActivity(new Intent(this, ViewOrders.class));
                break;
        }
    }
}