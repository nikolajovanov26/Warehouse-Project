package com.example.warehouseproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderViewProducts extends RecyclerView.ViewHolder{

    TextView name, price, company;

    public ViewHolderViewProducts(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.productName);
        price = itemView.findViewById(R.id.productPrice);
        company = itemView.findViewById(R.id.productCompany);
    }
}
