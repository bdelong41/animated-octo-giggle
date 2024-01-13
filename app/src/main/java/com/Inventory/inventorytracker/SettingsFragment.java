package com.Inventory.inventorytracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Inventory.inventorytracker.DAO.DBHandler;
import com.Inventory.inventorytracker.model.Box;
import com.Inventory.inventorytracker.model.MyListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends ListFragment {
    //Layout Vars
    private ListView listView;
    private TextView nameView;
    //buttons
    private Button addButton;
    private Button deleteButton;
    private FloatingActionButton addFab;
    private FloatingActionButton saveFab;
    private FloatingActionButton dbfab;
    private FrameLayout frameLayout;
    private EditText owner;
    private EditText description;

    //local vars
    private Context context;
    private DBHandler dbHandler;
    private Integer selectedID;
    private static MyListAdapter adapter;
    private static Box selected;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment(Integer boxID) {
        selectedID = boxID;
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
        SettingsFragment fragment = new SettingsFragment(null);
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
        //retrieving data
        dbHandler = new DBHandler(getActivity());
        selected = new Box("Owner", new ArrayList<String>(Arrays.asList("")), 1, 1, "");
        dbHandler.getPackage(selected.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //component initialization
        // Inflate the layout for this fragment
        frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_settings, container, false);
        addFab = frameLayout.findViewById(R.id.add);
        saveFab = frameLayout.findViewById(R.id.save);
        dbfab = frameLayout.findViewById(R.id.addDatabase);
        owner = frameLayout.findViewById(R.id.owner);
        description = frameLayout.findViewById(R.id.description);

        //ListView initialization
        dbHandler = new DBHandler(getActivity());
        if (selectedID != null && selectedID != 0) {
            selected = dbHandler.getPackage(selectedID);
        }
        //creating new box if one doesn't exist
        if(selected == null){
            adapter = new MyListAdapter(getActivity(), new ArrayList<String>());
            adapter.addItem();
            if (selectedID > 0) selected = dbHandler.createBox(selectedID);
        }
        else {
            adapter = new MyListAdapter(getActivity(), selected.getContents());
            adapter.addItem();
        }
        setListAdapter(adapter);
        //setting button listeners
        addFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                adapter.addItem();
                setListAdapter(adapter);
            }
        });
        dbfab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                List<String> content = new ArrayList<String>(Arrays.asList("contents1", "contents2", "contents3"));
                for(int index = 0; index < 10; index++){
                    dbHandler.addNewData("Name" + Integer.toString(index), content, "Person's package", index);
                }
            }
        });
        saveFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                selected.setContents(adapter.getVals());
                selected.setOwner(owner.toString());
                selected.setDescription(description.toString());
                dbHandler.updateData(selected);
            }
        });

        //initializing owner and description
        owner.setText("Owner: " + selected.getOwner());
        description.setText("Description: " + selected.getDescription());
        return frameLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

}