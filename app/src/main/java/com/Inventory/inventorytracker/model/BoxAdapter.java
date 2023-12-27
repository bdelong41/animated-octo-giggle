package com.Inventory.inventorytracker.model;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.Inventory.inventorytracker.MainActivity;
import com.Inventory.inventorytracker.R;

import java.util.Arrays;
import java.util.List;

public class BoxAdapter extends ArrayAdapter<Box> {
    private final Context context;
    private static List<Box> values;
    private AdapterView.OnItemClickListener onItemClickListener;
    public BoxAdapter(Context context, List<Box> values) {
        super(context, R.layout.plain_list_item, values);
        this.context = context;
        this.values = values;
    }

    public List<Box> getVals(){
        return this.values;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.plain_list_item, parent, false);

        final EditText editText = (EditText) rowView.findViewById(R.id.edit_text);

        editText.setText(values.get(position).toString());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(null, rowView, position, -1);
                }
            }
        });


        return rowView;
    }
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
