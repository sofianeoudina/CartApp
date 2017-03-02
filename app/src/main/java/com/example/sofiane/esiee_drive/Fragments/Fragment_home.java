package com.example.sofiane.esiee_drive.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sofiane.esiee_drive.Adapter.FileInfosAdapter;
import com.example.sofiane.esiee_drive.Classes.Directory;
import com.example.sofiane.esiee_drive.Classes.FileInfos;
import com.example.sofiane.esiee_drive.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.view.View.VISIBLE;

/**
 * Created by Sofiane on 16/01/2017.
 */

public class Fragment_home extends Fragment {
    private ListView lv;
    private ArrayList<FileInfos> listItems = new ArrayList<FileInfos>();
    private FileInfosAdapter adapter;
    private View view;
    private TextView no_historical;
    String yearFolder = "directories";

    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private ArrayList<String> items_years = new ArrayList<String>();
    private List<Object> values = new ArrayList<Object>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Accueil");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRef = mDatabase.child("directories");

        lv = (ListView) view.findViewById(R.id.historical_list);
        adapter = new FileInfosAdapter(view.getContext(), android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);

        no_historical = (TextView) view.findViewById(R.id.no_historical);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearFolder = items_years.get(position);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, Fragment_Subject.newInstance(yearFolder), "fragment_Subject");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        readDataFromDatabase();
    }

    public void onFileInfosValidation(String file_name, String date){
        FileInfos f = new FileInfos(file_name, date);
        listItems.add(0, f);
        adapter.notifyDataSetChanged();
    }

    private void clearListView(){
        listItems.clear();
        adapter.notifyDataSetChanged();
    }

    private void isFileUploaded(){
        if(lv.getAdapter().getCount() == 0)
            no_historical.setVisibility(View.VISIBLE);
        else
            no_historical.setVisibility(View.GONE);
    }

    private void readDataFromDatabase(){
        Query myTopPostsQuery = mRef;
        //ecentPostsQuery = mDataRef.child("directories").child(yearName).child("subject").child(subjectName).child("course").orderByChild("name");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearListView();
                items_years.clear();

                Map<String, Object> objectMap = (HashMap<String,Object>) dataSnapshot.getValue();
                List<Object> list = new ArrayList<Object>();

                for (Object obj : objectMap.values()) {
                    Log.v("test",""+ obj);
                }

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    //Log.v("test",""+ childDataSnapshot.getKey()); //displays the key for the node
                    //Log.v("test",""+ childDataSnapshot.child("subject").getValue());

                    String date = childDataSnapshot.child("creationDate").child("time").getValue().toString();

                    Format sdf = new SimpleDateFormat("dd/MM/yyyy");
                    long timestamp = Long.parseLong(date);
                    Date netDate = (new Date(timestamp));

                    items_years.add(childDataSnapshot.getKey().toString());
                    onFileInfosValidation(childDataSnapshot.getKey().toString(), sdf.format(netDate));
                }
                isFileUploaded();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void readDataFromDatabase_2(String yearName){
        Query postsQuery = mRef.child("directories").child(yearName).child("subject");
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v("test",""+ childDataSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void printArray(){
        for(int i = 0; i < items_years.size(); i++){
            Log.v("v = ", "test = " + items_years.get(i));
        }
    }

}
