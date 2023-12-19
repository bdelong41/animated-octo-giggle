package com.Inventory.inventorytracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Inventory.inventorytracker.DataBase.DBHandler;
import com.Inventory.inventorytracker.model.Box;
import com.Inventory.inventorytracker.model.ScannedItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //xml vars
    private ListView listView;

    private Button addButton;
    private Button deleteButton;
    private TextView nameView;

    //local vars
    private ArrayAdapter<String> adapter;
    private String[] contents;
    private Context context;

    private ArrayList<String> currentContents;

    private DBHandler dbHandler;
    private ScannedItem scannedItem;
    private Box currentBox;

    private FloatingActionButton addFab;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.fragment_settings, container, false);
        addFab = ll.findViewById(R.id.add);

        dbHandler = new DBHandler(getActivity());
        Box box = dbHandler.getData(100);
        String fruits[] = {"apple", "Bread"};
//        adapter = new ArrayAdapter<String>(context, R.id.listViewLinearLayout, contents);
//        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.fruits_list, android.R.layout.simple_list_item_1);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, box.getContents());
        setListAdapter(arrayAdapter);
        return ll;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void loadData(){
        currentBox = dbHandler.getData(scannedItem.getBoxID());
    }
}