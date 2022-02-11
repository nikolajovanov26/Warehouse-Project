package com.example.warehouseproject;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderOrders extends RecyclerView.ViewHolder{

    TextView id, status, price;
    Button viewOrder;

    public ViewHolderOrders(@NonNull View itemView) {
        super(itemView);

        id = itemView.findViewById(R.id.orderId);
        status = itemView.findViewById(R.id.orderStatus);
        price = itemView.findViewById(R.id.orderPrice);
        viewOrder = itemView.findViewById(R.id.viewOrder);
    }
}
