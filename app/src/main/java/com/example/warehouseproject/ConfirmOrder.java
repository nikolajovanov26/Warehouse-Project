package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ConfirmOrder extends AppCompatActivity implements View.OnClickListener {

    Button main, warehouse, orders;
    Button confirmOrder;
    Integer prodNum;
    int i;
    Integer totalPrice = 0, totalWeight = 0;
    HashMap price,weight,status;
    TextView total;

    private DatabaseReference reference, reference2;
    private DatabaseReference ref, ref2;
    private DatabaseReference r, r2;

    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, ViewHolderConfirmOrder> adapter;
    private RecyclerView recyclerView;
    String orderKey;
    int counter = 0;

    ArrayList<Product> list;
    AdapterConfirmOrder myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Intent intent = getIntent();
        orderKey = intent.getStringExtra("key");

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        warehouse = findViewById(R.id.warehouse);
        warehouse.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);

        total = findViewById(R.id.orderTotal);

        confirmOrder = findViewById(R.id.confirmOrder);
        confirmOrder.setOnClickListener(this);

        reference = FirebaseDatabase.getInstance().getReference("Orders");
        reference2 = reference.child(orderKey).child("prodNum");

        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new AdapterConfirmOrder(this, list);
        recyclerView.setAdapter(myAdapter);

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prodNum = snapshot.getValue(Integer.class);
                findProducts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void findProducts() {
        for(i=0;i<prodNum;i++){
            String productNo = "Product"+(i+1);

            ref = FirebaseDatabase.getInstance().getReference("Orders");
            ref2 = ref.child(orderKey).child(productNo);
            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String productStringNameQty = snapshot.getValue(String.class);
                    sendProdData(productStringNameQty);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void sendProdData(String productStringNameQty) {
        String[] prodData = productStringNameQty.split("@@@@@");
        //Log.d("product", "key: "+prodData[0]+" value: "+prodData[1]);


        r = FirebaseDatabase.getInstance().getReference("Products");
        Query query = r.orderByChild("id").equalTo(prodData[0]);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){

                    Product product = snap.getValue(Product.class);
                    int qt = Integer.parseInt(prodData[1]);
                    product.setQty(qt);
                    totalWeight += product.getWeight()*(Integer.parseInt(prodData[1]));
                    totalPrice += product.getPrice()*(Integer.parseInt(prodData[1]));

                    list.add(product);

                }
                myAdapter.notifyDataSetChanged();
                total.setText(totalPrice.toString());
                updateDatabase();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateDatabase(){
        price = new HashMap();
        weight = new HashMap();
        price.put("totalPrice",totalPrice);
        weight.put("totalWeight",totalWeight);

        r = FirebaseDatabase.getInstance().getReference("Orders");
        r.child(orderKey).updateChildren(price).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });
        r = FirebaseDatabase.getInstance().getReference("Orders");
        r.child(orderKey).updateChildren(weight).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });
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
                startActivity(new Intent(this, ViewOrders.class));
                break;
            case R.id.confirmOrder:
                status = new HashMap();
                status.put("status","Confirmed");

                r2 = FirebaseDatabase.getInstance().getReference("Orders");
                r2.child(orderKey).updateChildren(status).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                    }
                });
                Toast.makeText(this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ViewOrders.class));
                break;
        }
    }
}