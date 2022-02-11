package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainDriver extends AppCompatActivity implements View.OnClickListener {

    Button main, find, orders;
    Button logout;

    TextView name, phone;

    private FirebaseUser user;
    private DatabaseReference reference,reference2;
    private String userID;
    HashMap id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        find = findViewById(R.id.findOrders);
        find.setOnClickListener(this);
        orders = findViewById(R.id.viewOrders);
        orders.setOnClickListener(this);
        logout = findViewById(R.id.logOut);
        logout.setOnClickListener(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfil = snapshot.getValue(User.class);

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

                name.setText(nameDb);
                phone.setText(phoneDb);
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
                //startActivity(new Intent(this, MainDriver.class));
                break;
            case R.id.findOrders:
                startActivity(new Intent(this, FindOrders.class));
                break;
            case R.id.viewOrders:
                startActivity(new Intent(this, ViewOrdersDriver.class));
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}