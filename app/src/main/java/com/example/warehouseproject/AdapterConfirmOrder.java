package com.example.warehouseproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterConfirmOrder extends RecyclerView.Adapter<AdapterConfirmOrder.MyViewHolder> {

    static Context context;
    ArrayList<Product> list;

    public AdapterConfirmOrder(Context context, ArrayList<Product> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.products_confirm,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Product product = list.get(position);

        holder.productName.setText(product.getProduct());
        holder.productPrice.setText(product.getPrice()+"");
        holder.productCompany.setText(product.getCompany());
        holder.quantity.setText(product.getQty()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView productName, productPrice, productCompany, quantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCompany = itemView.findViewById(R.id.productCompany);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}
