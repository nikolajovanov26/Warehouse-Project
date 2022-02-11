package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewOrdersWarehouse extends AppCompatActivity implements View.OnClickListener {

    Button main, products, orders;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, ViewHolderOrders> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_warehouse);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        products = findViewById(R.id.products);
        products.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Orders");

        recyclerView = findViewById(R.id.viewOrders);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = reference.orderByChild("warehouseId").equalTo(userID);

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
                holder.id.setText(model.getId());
                holder.status.setText(model.getStatus());
                holder.price.setText(model.getTotalPrice()+"");

                holder.viewOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent intent = new Intent(view.getContext(), ViewOrder.class);
                        //intent.putExtra("id", model.getId());
                        //intent.putExtra("name", model.getName());
                        //view.getContext().startActivity(intent);
                        Toast.makeText(ViewOrdersWarehouse.this, "id: "+model.getId(), Toast.LENGTH_SHORT).show();
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