package com.example.warehouseproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolderConfirmOrder extends RecyclerView.ViewHolder{

    TextView productName, productPrice, productCompany, quantity;

    public ViewHolderConfirmOrder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.productName);
        productPrice = itemView.findViewById(R.id.productPrice);
        productCompany = itemView.findViewById(R.id.productCompany);
        quantity = itemView.findViewById(R.id.quantity);
    }
}
