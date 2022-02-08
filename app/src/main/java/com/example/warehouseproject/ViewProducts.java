package com.example.warehouseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ViewProducts extends AppCompatActivity implements View.OnClickListener {

    Button main, warehouse, orders;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        Intent intent = getIntent();
        String whName = intent.getStringExtra("name");

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        warehouse = findViewById(R.id.warehouse);
        warehouse.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);

        name = findViewById(R.id.warehouseName);
        name.setText(whName);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main:
                startActivity(new Intent(this,  MainStore.class));
                break;
            case R.id.warehouse:
                startActivity(new Intent(this, ViewWarehouse.class));
                break;
            case R.id.orders:
                //startActivity(new Intent(this, ViewOrders.class));
                break;
        }
    }
}