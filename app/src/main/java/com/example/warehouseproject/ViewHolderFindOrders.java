package com.example.warehouseproject;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class ViewHolderFindOrders extends RecyclerView.ViewHolder {

    TextView wName, wRating, sName, sRating, d1, d2, price, weight;
    Button apply;

    public ViewHolderFindOrders(@NonNull View itemView) {
        super(itemView);

        wName = itemView.findViewById(R.id.warehouseName);
        wRating = itemView.findViewById(R.id.warehouseRating);
        sName = itemView.findViewById(R.id.storeName);
        sRating = itemView.findViewById(R.id.storeRating);
        d1 = itemView.findViewById(R.id.distance1);
        d2 = itemView.findViewById(R.id.distance2);
        price = itemView.findViewById(R.id.price);
        weight = itemView.findViewById(R.id.weight);
        apply = itemView.findViewById(R.id.apply);
    }
}
