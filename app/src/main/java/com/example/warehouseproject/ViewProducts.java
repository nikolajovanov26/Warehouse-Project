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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewProducts extends AppCompatActivity implements View.OnClickListener {

    Button main, warehouse, orders;
    TextView name;

    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;

    private FirebaseRecyclerOptions<Product> options;
    private FirebaseRecyclerAdapter<Product, ViewHolderViewProducts> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        Intent intent = getIntent();
        String whName = intent.getStringExtra("name");
        String whId = intent.getStringExtra("id");

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        warehouse = findViewById(R.id.warehouse);
        warehouse.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);

        name = findViewById(R.id.warehouseName);
        name.setText(whName);

        recyclerView = findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Products");
        Query query = reference.orderByChild("warehouseId").equalTo(whId);

        options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(query,Product.class).build();
        adapter = new FirebaseRecyclerAdapter<Product, ViewHolderViewProducts>(options) {

            @NonNull
            @Override
            public ViewHolderViewProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products,parent,false);
                return new ViewHolderViewProducts(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderViewProducts holder, int position, @NonNull Product model) {
                holder.name.setText(model.getProduct());
                holder.price.setText(model.getPrice()+"");
                holder.company.setText(model.getCompany());
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

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