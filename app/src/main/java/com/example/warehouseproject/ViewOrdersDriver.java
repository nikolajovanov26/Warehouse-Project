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

import java.util.HashMap;

public class ViewOrdersDriver extends AppCompatActivity implements View.OnClickListener {

    Button main, find, orders;

    private FirebaseUser user;
    private DatabaseReference reference,reference2;
    private String userID;

    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, ViewHolderMyOrders> adapter;
    private RecyclerView recyclerView;

    HashMap status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_driver);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        find = findViewById(R.id.findOrders);
        find.setOnClickListener(this);
        orders = findViewById(R.id.viewOrders);
        orders.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Orders");

        recyclerView = findViewById(R.id.viewOrdersRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = reference.orderByChild("driverId").equalTo(userID);
        options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(query,Order.class).build();
        adapter = new FirebaseRecyclerAdapter<Order, ViewHolderMyOrders>(options) {

            @NonNull
            @Override
            public ViewHolderMyOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_my_orders,parent,false);
                return new ViewHolderMyOrders(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderMyOrders holder, int position, @NonNull Order model) {
                reference2 = FirebaseDatabase.getInstance().getReference("Users");
                Query q1 = reference2.orderByChild("id").equalTo(model.getWarehouseId());
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            User user = snap.getValue(User.class);
                            Log.d("User 1", user.toString() + "");
                            holder.warehouse.setText(user.getName());

                            Query q = reference2.orderByChild("id").equalTo(model.getStoreId());
                            q.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    for (DataSnapshot snap1 : snapshot1.getChildren()) {
                                        User user2 = snap1.getValue(User.class);
                                        Log.d("User 2", user2.toString() + "");
                                        holder.store.setText(user2.getName());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.finishOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        status =  new HashMap();
                        status.put("driverId",userID+"@@@@@FINISHED");

                        reference = FirebaseDatabase.getInstance().getReference("Orders");
                        reference.child(model.getId()).updateChildren(status).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                            }
                        });


                        Intent intent = new Intent(view.getContext(), RateWareStore.class);
                        intent.putExtra("id", model.getId());
                        view.getContext().startActivity(intent);
                        Toast.makeText(ViewOrdersDriver.this, "id: "+model.getId(), Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(this, MainDriver.class));
                break;
            case R.id.findOrders:
                startActivity(new Intent(this, FindOrders.class));
                break;
            case R.id.viewOrders:
                //startActivity(new Intent(this, ViewOrdersDriver.class));
                break;
        }
    }
}