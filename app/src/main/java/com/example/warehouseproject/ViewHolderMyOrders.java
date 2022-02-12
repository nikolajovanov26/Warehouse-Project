package com.example.warehouseproject;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderMyOrders extends RecyclerView.ViewHolder {

    TextView warehouse, store;
    Button finishOrder;

    public ViewHolderMyOrders(@NonNull View itemView) {
        super(itemView);

        warehouse = itemView.findViewById(R.id.warehouseName);
        store = itemView.findViewById(R.id.storeName);
        finishOrder = itemView.findViewById(R.id.finishOrder);

    }
}
