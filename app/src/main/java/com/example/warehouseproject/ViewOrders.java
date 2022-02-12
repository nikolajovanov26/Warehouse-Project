package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class ViewOrders extends AppCompatActivity implements View.OnClickListener {

    Button main, warehouse, orders;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    HashMap confirm;

    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, ViewHolderOrders> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        warehouse = findViewById(R.id.warehouse);
        warehouse.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Orders");

        recyclerView = findViewById(R.id.viewOrders);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = reference.orderByChild("storeId").equalTo(userID);

        options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(query,Order.class).build();
        adapter = new FirebaseRecyclerAdapter<Order, ViewHolderOrders>(options) {

            @NonNull
            @Override
            public ViewHolderOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders,parent,false);
                return new ViewHolderOrders(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderOrders holder, int position, @NonNull Order model) {

                if(!model.getStatus().equals("Not confirmed") ){
                    holder.viewOrder.setVisibility(View.GONE);
                }
                holder.id.setText(model.getId());
                holder.status.setText(model.getStatus());
                holder.price.setText(model.getTotalPrice()+"");



                holder.viewOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirm = new HashMap();

                        confirm.put("status","Confirmed");

                        reference = FirebaseDatabase.getInstance().getReference("Orders");
                        reference.child(model.getId()).updateChildren(confirm).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                            }
                        });
                        Toast.makeText(ViewOrders.this, "id: "+model.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main:
                startActivity(new Intent(this, MainStore.class));
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