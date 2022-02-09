package com.example.warehouseproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class AdapterConfirmProducts extends BaseAdapter {
    private final ArrayList mData;

    public AdapterConfirmProducts(Map<String,Integer> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return (Map.Entry) mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_confirm, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Integer> item = (Map.Entry<String, Integer>) getItem(position);

        //TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.productName)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.productPrice)).setText(item.getValue());

        return result;
    }
}
