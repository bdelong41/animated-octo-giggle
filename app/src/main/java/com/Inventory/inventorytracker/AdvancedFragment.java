
package com.Inventory.inventorytracker;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SearchView;

import com.Inventory.inventorytracker.DAO.DBHandler;
import com.Inventory.inventorytracker.model.Box;
import com.Inventory.inventorytracker.model.BoxAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdvancedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdvancedFragment extends Fragment {

    //layout components
    private SearchView searchView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RelativeLayout relativeLayout;

    private List<Box> boxes;
    private List<Box> filteredList;
    private BoxAdapter boxAdapter;

    private ArrayAdapter<Box> adapter;
    private ListView listView;

    public AdvancedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdvancedFragment newInstance(String param1, String param2) {
        AdvancedFragment fragment = new AdvancedFragment();
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
        DBHandler dbHandler = new DBHandler(getActivity());
        boxes = dbHandler.getAllPackages();
        if(boxes.size() == 0) return relativeLayout;
        // Inflate the layout for this fragment
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_advanced, container, false);
        listView = (ListView) relativeLayout.findViewById(R.id.listView);
        searchView = relativeLayout.findViewById(R.id.search_bar);
        //initializing adapter
        adapter = new ArrayAdapter<Box>(getActivity(), R.layout.simple_list_item_1, dbHandler.getAllPackages());
        listView.setAdapter(adapter);
        //setting onclick listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList = dbHandler.getRelated(newText);
                listView.setAdapter(createAdapter(getActivity(), filteredList));
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Log.d("ListItemClick", "clicked");
                Box selected = boxes.get(pos);
                ((MainActivity)getActivity()).openSettings(selected.getBoxID());
            }
        });
        return relativeLayout;
    }

    ArrayAdapter<Box> createAdapter(Activity activity, List<Box> items){
        ArrayAdapter<Box> adapter = new ArrayAdapter<>(activity,R.layout.simple_list_item_1, items);
        return adapter;
    }
}