package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.location.LocationListener;
import android.location.LocationManager;
import android.app.Activity;
import android.content.Context;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FindOrders extends AppCompatActivity implements View.OnClickListener {

    Button main, find, orders;

    private FirebaseUser user;
    private DatabaseReference reference, reference2, r;
    private String userID;
    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, ViewHolderFindOrders> adapter;
    private RecyclerView recyclerView;
    DecimalFormat rating;

    HashMap status, driverIdAdd;

    FusedLocationProviderClient fusedLocationProviderClient;
    Double lat0, lat1, lat2;
    Double lon0, lon1, lon2;

    Location startPoint, middlePoint, endPoint;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_orders);

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        find = findViewById(R.id.findOrders);
        find.setOnClickListener(this);
        orders = findViewById(R.id.viewOrders);
        orders.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Orders");
        userID = user.getUid();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            },1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat0 = location.getLatitude();
                lon0 = location.getLongitude();
            }
        });


        recyclerView = findViewById(R.id.orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = reference.orderByChild("status").equalTo("Confirmed");

        options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(query, Order.class).build();
        adapter = new FirebaseRecyclerAdapter<Order, ViewHolderFindOrders>(options) {

            @NonNull
            @Override
            public ViewHolderFindOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_orders, parent, false);
                return new ViewHolderFindOrders(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderFindOrders holder, int position, @NonNull Order model) {

                holder.price.setText(model.getTotalPrice() + "MKD");
                holder.weight.setText(model.getTotalWeight() / 1000 + "kg");

                reference2 = FirebaseDatabase.getInstance().getReference("Users");
                Query query = reference2.orderByChild("id").equalTo(model.getWarehouseId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {

                            User user = snap.getValue(User.class);

                            holder.wName.setText(user.getName());
                            rating = new DecimalFormat("0.0");
                            holder.wRating.setText(rating.format(user.getRating()));

                            Query query2 = reference2.orderByChild("id").equalTo(model.getStoreId());
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap : snapshot.getChildren()) {
                                        lon1 = user.getLon();
                                        lat1 = user.getLat();

                                        startPoint = new Location("location A");
                                        startPoint.setLatitude(lat0);
                                        startPoint.setLongitude(lon0);

                                        middlePoint = new Location("location B");
                                        middlePoint.setLatitude(lat1);
                                        middlePoint.setLongitude(lon1);

                                        User user2 = snap.getValue(User.class);
                                        lon2 = user2.getLon();
                                        lat2 = user2.getLat();

                                        endPoint = new Location("location C");
                                        endPoint.setLatitude(lat2);
                                        endPoint.setLongitude(lon2);

                                        holder.sName.setText(user2.getName());

                                        holder.sRating.setText(rating.format(user2.getRating()));

                                        double distance = startPoint.distanceTo(middlePoint);
                                        double dist = distance / 1000.0;
                                        int dis = (int) Math.round(dist);
                                        holder.d1.setText(dis + " km");

                                        double distance2 = middlePoint.distanceTo(endPoint);
                                        double dist2 = distance2 / 1000.0;
                                        int dis2 = (int) Math.round(dist2);
                                        holder.d2.setText(dis2 + " km");
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



                holder.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        status = new HashMap();
                        status.put("status", "In progress");
                        driverIdAdd = new HashMap();
                        driverIdAdd.put("driverId", userID);

                        r = FirebaseDatabase.getInstance().getReference("Orders");
                        r.child(model.getId()).updateChildren(status).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                            }
                        });

                        r = FirebaseDatabase.getInstance().getReference("Orders");
                        r.child(model.getId()).updateChildren(driverIdAdd).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                            }
                        });
                        Intent intent = new Intent(view.getContext(), ViewOrdersDriver.class);
                        view.getContext().startActivity(intent);
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
                //startActivity(new Intent(this, FindOrders.class));
                break;
            case R.id.viewOrders:
                startActivity(new Intent(this, ViewOrdersDriver.class));
                break;
        }
    }
}