package com.Inventory.inventorytracker.model;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.Inventory.inventorytracker.R;

import java.util.Arrays;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private static List<String> values;

    public MyListAdapter(Context context, String[] values) {
        super(context, R.layout.list_item, Arrays.asList(values));
        this.context = context;
        this.values = Arrays.asList(values);
    }
    public MyListAdapter(Context context, List<String> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    public void addItem(){
        this.values.add("");
    }

    public List<String> getVals(){
        return this.values;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        final EditText editText = (EditText) rowView.findViewById(R.id.edit_text);

        editText.setText(values.get(position));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                values.set(position, editText.getText().toString());
            }
        });

        return rowView;
    }
}
