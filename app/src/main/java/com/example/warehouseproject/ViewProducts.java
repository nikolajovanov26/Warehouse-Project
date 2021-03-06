package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewProducts extends AppCompatActivity implements View.OnClickListener {

    Button main, warehouse, orders;
    Button placeOrder;
    TextView name;

    Integer qty;

    private DatabaseReference reference, reference2;
    private FirebaseUser user;
    private String userID;

    private FirebaseRecyclerOptions<Product> options;
    private FirebaseRecyclerAdapter<Product, ViewHolderViewProducts> adapter;
    private RecyclerView recyclerView;

    Map<String, Integer> cart = new HashMap<>();
    String current;

    Integer prodNum,totalPrice,totalWeight;
    HashMap putProduct;

    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        Intent intent = getIntent();
        String whName = intent.getStringExtra("name");
        String whId = intent.getStringExtra("id");

        prodNum = 0;
        totalPrice = 0;
        totalWeight =0;
        i=0;

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        warehouse = findViewById(R.id.warehouse);
        warehouse.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);

        placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(this);

        name = findViewById(R.id.warehouseName);
        name.setText(whName);

        recyclerView = findViewById(R.id.products);
        //recyclerView.setHasFixedSize(true);
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
                qty = 0;

                holder.qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        current = holder.qty.getText().toString();
                        if(!current.isEmpty()){
                            qty = Integer.parseInt(current);
                            if(qty>0){
                                if(cart.get(model.getId()) != null)
                                {
                                    cart.remove(model.getId());
                                    cart.put(model.getId(),qty);
                                } else {
                                    cart.put(model.getId(),qty);
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                holder.name.setText(model.getProduct());
                holder.price.setText(model.getPrice()+"");
                holder.company.setText(model.getCompany());



            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

        refresh();


    }

    private void refresh() {
        adapter.notifyDataSetChanged();
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
            case R.id.placeOrder:
                String key = placeOrder();
                Intent intent = new Intent(this, ConfirmOrder.class);
                intent.putExtra("key", key);
                startActivity(intent);
                break;
        }
    }

    private String placeOrder() {
        prodNum=0;
        i=1;
        reference2 = FirebaseDatabase.getInstance().getReference("Orders");

        String storeId = userID;
        Intent intent = getIntent();
        String whId = intent.getStringExtra("id");

        String key = reference2.push().getKey();

        for(Map.Entry<String, Integer> entry : cart.entrySet()){
            prodNum++;
        }

        Order order = new Order(key,storeId,whId,prodNum);

        FirebaseDatabase.getInstance().getReference("Orders")
                .child(key)
                .setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });



        for(Map.Entry<String, Integer> entry : cart.entrySet()){
            putProduct = new HashMap();
            String productStr = entry.getKey()+"@@@@@"+entry.getValue();
            String prodNumber = "Product"+i;
            putProduct.put(prodNumber,productStr);
            reference2 = FirebaseDatabase.getInstance().getReference("Orders");
            reference2.child(key).updateChildren(putProduct).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                }
            });
            i++;
        }

        return key;
    }
}