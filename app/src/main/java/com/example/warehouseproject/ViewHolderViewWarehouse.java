package com.example.warehouseproject;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ViewHolderViewWarehouse extends RecyclerView.ViewHolder {

    TextView name, location, rating;
    Button viewProducts;

    public ViewHolderViewWarehouse(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.warehouseName);
        location = itemView.findViewById(R.id.warehouseLocation);
        rating = itemView.findViewById(R.id.warehouseRating);
        viewProducts = itemView.findViewById(R.id.viewProducts);
    }
}
