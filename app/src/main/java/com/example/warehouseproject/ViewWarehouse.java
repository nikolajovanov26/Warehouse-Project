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
import com.google.firebase.database.ValueEventListener;

public class ViewWarehouse extends AppCompatActivity implements View.OnClickListener {

    Button main, warehouse, orders;
    Button placeOrder;

    TextView name, phone, location;

    private FirebaseUser user;
    private DatabaseReference reference, reference2;
    private String userID;

    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, ViewHolderViewWarehouse> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_warehouse);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        warehouse = findViewById(R.id.warehouse);
        warehouse.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);
        placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        recyclerView = findViewById(R.id.warehouses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = reference.orderByChild("type").equalTo("Warehouse Manager");

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query,User.class).build();
        adapter = new FirebaseRecyclerAdapter<User, ViewHolderViewWarehouse>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderViewWarehouse holder, int position, @NonNull User model) {
                holder.name.setText(model.getName());
                holder.location.setText(model.getLokacija());
                if(model.getRating()!=null){
                    holder.rating.setText(model.getRating().toString());
                }
                holder.viewProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ViewProducts.class);
                        intent.putExtra("id", model.getId());
                        intent.putExtra("name", model.getName());
                        view.getContext().startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolderViewWarehouse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouses,parent,false);
                return new ViewHolderViewWarehouse(view);
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
                //startActivity(new Intent(this, AddProducts.class));
                break;
            case R.id.orders:
                //startActivity(new Intent(this, ViewOrders.class));
                break;
            case R.id.placeOrder:
                //FirebaseAuth.getInstance().signOut();
                //startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}