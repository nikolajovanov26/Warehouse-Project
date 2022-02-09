package com.example.warehouseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfirmOrder extends AppCompatActivity implements View.OnClickListener {

    Button main, warehouse, orders;
    Button confirmOrder;

    Map<String, Integer> cart = new HashMap<>();
    ListView products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Intent intent = getIntent();
        cart = (Map<String, Integer>) intent.getSerializableExtra("cart");

        for(Map.Entry<String, Integer> entry : cart.entrySet()){
            String key = entry.getKey();
            Integer qty = entry.getValue();
            Log.d("transferred", key+" "+qty);
        }

        main = findViewById(R.id.main);
        main.setOnClickListener(this);
        warehouse = findViewById(R.id.warehouse);
        warehouse.setOnClickListener(this);
        orders = findViewById(R.id.orders);
        orders.setOnClickListener(this);

        confirmOrder = findViewById(R.id.confirmOrder);
        confirmOrder.setOnClickListener(this);

        products = findViewById(R.id.products);

        List<HashMap<String,String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.products_confirm,
                new String[]{"Name","Qty"},
                new int[]{R.id.productName,R.id.quantity});

        Iterator it = cart.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("Name",pair.getKey().toString());
            resultMap.put("Qty",pair.getValue().toString());
            listItems.add(resultMap);
        }
        //AdapterConfirmProducts adapter = new AdapterConfirmProducts(cart);
        //products.setAdapter(adapter);


        products.setAdapter(adapter);
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
                //startActivity(new Intent(this, ViewOrders.class));
                break;
            case R.id.placeOrder:
                Toast.makeText(this, "Placed Order", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(this, ViewOrders.class));
                break;
        }
    }
}