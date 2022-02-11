package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProducts extends AppCompatActivity implements View.OnClickListener {

    Button main, products, orders;
    Button addProduct;
    EditText product, company, price, weight, stock;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        products = findViewById(R.id.products);
        products.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);
        addProduct = findViewById(R.id.addProduct);
        addProduct.setOnClickListener(this);

        product = findViewById(R.id.product);
        company = findViewById(R.id.company);
        price = findViewById(R.id.price);
        weight = findViewById(R.id.weight);
        stock = findViewById(R.id.stock);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main:
                startActivity(new Intent(this, MainWarehouse.class));
                break;
            case R.id.products:
                //startActivity(new Intent(this, AddProducts.class));
                break;
            case R.id.orders:
                startActivity(new Intent(this, ViewOrdersWarehouse.class));
                break;
            case R.id.addProduct:
                addProduct();
                break;
        }
    }

    private void addProduct() {
        String product_db = product.getText().toString();
        String company_db = company.getText().toString();
        String price_str = price.getText().toString();
        String weight_str = weight.getText().toString();
        String stock_str = stock.getText().toString();

        if(product_db.isEmpty()){
            product.setError("Enter Product");
            product.requestFocus();
            return;
        }
        if(company_db.isEmpty()){
            company.setError("Enter Company");
            company.requestFocus();
            return;
        }
        if(price_str.isEmpty()){
            price.setError("Enter Price");
            price.requestFocus();
            return;
        }
        if(weight_str.isEmpty()){
            weight.setError("Enter Weight");
            weight.requestFocus();
            return;
        }
        if(stock_str.isEmpty()){
            stock.setError("Enter Stock");
            stock.requestFocus();
            return;
        }

        int price_db = Integer.parseInt(price_str);
        int weight_db = Integer.parseInt(weight_str);
        int stock_db = Integer.parseInt(stock_str);
        int test = price_db + weight_db + stock_db;




        String id = (String) userID.toString();
        String key = reference.push().getKey();
        Product product = new Product(key, product_db, company_db, price_db, weight_db, stock_db, userID);

        FirebaseDatabase.getInstance().getReference("Products")
                .child(key)
                .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddProducts.this, "Product has been added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddProducts.this, MainWarehouse.class));
                } else {
                    Toast.makeText(AddProducts.this, "Error while adding product", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}