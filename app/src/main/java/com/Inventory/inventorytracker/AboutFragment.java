
package com.Inventory.inventorytracker;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

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

    String s[] = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
    };
    final ArrayList<String> list = new ArrayList<>();

    private List<Box> boxes;
    private List<Box> filteredList;
    private BoxAdapter boxAdapter;

    private ListView listView;

    public AboutFragment() {
        // Required empty public constructor
        for (int i = 0; i < s.length; i++) {
            list.add(s[i]);
        }


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
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        boxes = getData();
        if(boxes.size() == 0) return relativeLayout;
        // Inflate the layout for this fragment
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_about, container, false);
        listView = (ListView) relativeLayout.findViewById(R.id.listView);
        searchView = relativeLayout.findViewById(R.id.search_bar);
        BoxAdapter adapter = createAdapter(getActivity(), boxes);
        listView.setAdapter(adapter);

        //setting onclick listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList = new ArrayList<>();
                for(Box item: boxes){
                    //searching its flavor text
                    if(item.toString().toLowerCase().contains(newText)){
                        filteredList.add(item);
                    }
                    //searching its contents
                    else {
                        for(String content: item.getContents()){
                            if(content.toLowerCase().contains(newText)) {
                                filteredList.add(item);
                                break;
                            }
                        }
                    }
                }
                boxAdapter = createAdapter(getActivity(), filteredList);
                listView.setAdapter(boxAdapter);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Log.d("ListItemClick", "clicked");
                Box selected = boxAdapter.getItem(pos);
//                ((MainActivity)getActivity()).openSettings(selected.getBoxID());
            }
        });
        return relativeLayout;
    }
    List<Box> getData(){
        DBHandler dbHandler = new DBHandler(getActivity());
        return dbHandler.getAllPackages();
    }

    BoxAdapter createAdapter(Activity activity, List<Box> items){
        BoxAdapter adapter = new BoxAdapter(activity, items);
//        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("experimentTest", "Called on Click!");
////                ((MainActivity)getActivity()).openSettings(items.get(i).getBoxID());
//            }
//        });

        return adapter;
    }
}