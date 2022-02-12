package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RateWareStore extends AppCompatActivity {

    EditText store, warehouse;
    Integer storeR, warehouseR;
    Button save;
    String storeId, warehouseId;
    Query query;
    HashMap storeUpdate, warehouseUpdate;
    HashMap storeUpdate1, warehouseUpdate1;
    HashMap status;
    String st,wh;
    Integer numRatings, newNumRatings;
    Double currentRating, newCurrentRating;
    String orderId;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_ware_store);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("id");

        store = findViewById(R.id.store);
        warehouse = findViewById(R.id.warehouse);
        save = findViewById(R.id.save);

        reference = FirebaseDatabase.getInstance().getReference("Orders");
        query = reference.orderByChild("id").equalTo(orderId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Order order = snap.getValue(Order.class);
                    storeId = order.getStoreId();
                    warehouseId = order.getWarehouseId();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = store.getText().toString();
                wh = warehouse.getText().toString();
                if(wh.isEmpty()){
                    warehouse.setError("Rate Warehouse");
                    warehouse.requestFocus();
                    return;
                }
                if(st.isEmpty()){
                    store.setError("Rate Store");
                    store.requestFocus();
                    return;
                }

                storeR = Integer.parseInt(st);
                warehouseR = Integer.parseInt(wh);
                if(warehouseR<0.9 || warehouseR>5.1){
                    warehouse.setError("Value between 1 and 5");
                    warehouse.requestFocus();
                    return;
                }
                if(storeR<0.9 || storeR>5.1){
                    store.setError("Value between 1 and 5");
                    store.requestFocus();
                    return;
                }


                updateStore();
                updateWare();
                updateStatus();
                Intent intent = new Intent(view.getContext(), MainDriver.class);
                view.getContext().startActivity(intent);
            }
        });


    }

    public void updateStore() {

        reference = FirebaseDatabase.getInstance().getReference("Users");
        query = reference.orderByChild("id").equalTo(storeId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    //Log.d("test",user.toString());
                    numRatings = user.getNumRatings();
                    currentRating = user.getRating();
                    if(numRatings==null || currentRating==null){
                        newNumRatings = 1;
                        newCurrentRating = storeR*1.0;
                    } else {
                        newNumRatings = numRatings+1;
                        newCurrentRating = (numRatings*currentRating+storeR)/newNumRatings*1.0;
                    }

                    storeUpdate = new HashMap();
                    storeUpdate.put("rating", newCurrentRating);
                    storeUpdate1 = new HashMap();
                    storeUpdate1.put("numRatings", newNumRatings);

                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(storeId).updateChildren(storeUpdate).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(storeId).updateChildren(storeUpdate1).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void updateWare() {

        reference = FirebaseDatabase.getInstance().getReference("Users");
        query = reference.orderByChild("id").equalTo(warehouseId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    //Log.d("test",user.toString());
                    numRatings = user.getNumRatings();
                    currentRating = user.getRating();
                    if(numRatings==null || currentRating==null){
                        newNumRatings = 1;
                        newCurrentRating = storeR*1.0;
                    } else {
                        newNumRatings = numRatings+1;
                        newCurrentRating = (numRatings*currentRating+storeR)/newNumRatings*1.0;
                    }

                    warehouseUpdate = new HashMap();
                    warehouseUpdate.put("rating", newCurrentRating);
                    warehouseUpdate1 = new HashMap();
                    warehouseUpdate1.put("numRatings", newNumRatings);

                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(warehouseId).updateChildren(warehouseUpdate).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(warehouseId).updateChildren(warehouseUpdate1).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void updateStatus() {
        status =  new HashMap();
        status.put("status","Finished");
        status.put("storeId",storeId+"@@@@@FINISHED");
        status.put("warehouseId",warehouseId+"@@@@@FINISHED");

        reference = FirebaseDatabase.getInstance().getReference("Orders");
        reference.child(orderId).updateChildren(status).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });
    }
}