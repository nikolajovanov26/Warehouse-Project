package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainWarehouse extends AppCompatActivity implements View.OnClickListener {

    Button main, products, orders;
    Button logout;

    TextView name, phone, location;

    private FirebaseUser user;
    private DatabaseReference reference, reference2;
    private String userID;
    HashMap id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_warehouse);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        products = findViewById(R.id.products);
        products.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);
        logout = findViewById(R.id.logOut);
        logout.setOnClickListener(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfil = snapshot.getValue(User.class);

                if (userProfil != null) {
                    if (userProfil != null) {
                        id = new HashMap();
                        id.put("id",userID);
                        if(userProfil.id == null){
                            reference2 = FirebaseDatabase.getInstance().getReference("Users");
                            reference2.child(userID).updateChildren(id).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {

                                }
                            });
                        }
                    }

                    String nameDb = userProfil.name;
                    String phoneDb = userProfil.number;
                    String locationDB = userProfil.lokacija;

                    name.setText(nameDb);
                    phone.setText(phoneDb);
                    location.setText(locationDB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main:
                //startActivity(new Intent(this, MainWarehouse.class));
                break;
            case R.id.products:
                startActivity(new Intent(this, AddProducts.class));
                break;
            case R.id.orders:
                startActivity(new Intent(this, ViewOrdersWarehouse.class));
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}