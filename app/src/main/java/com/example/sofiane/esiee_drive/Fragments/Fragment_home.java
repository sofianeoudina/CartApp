package com.example.sofiane.esiee_drive.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sofiane.esiee_drive.Adapter.FileInfosAdapter;
import com.example.sofiane.esiee_drive.Classes.FileInfos;
import com.example.sofiane.esiee_drive.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    private DatabaseReference mDatabase;
    private DatabaseReference mRef;

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

        readDataFromDatabase();

        lv = (ListView) view.findViewById(R.id.historical_list);
        adapter = new FileInfosAdapter(view.getContext(), android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);

        no_historical = (TextView) view.findViewById(R.id.no_historical);

        onFileInfosValidation("Test", "Sofiane", "10/02/2017");

        isFileUploaded();
    }

    public void onFileInfosValidation(String file_name, String user_name, String date){
        FileInfos f = new FileInfos(file_name, user_name, date);
        listItems.add(0, f);
        adapter.notifyDataSetChanged();
    }

    private void isFileUploaded(){
        if(lv.getAdapter().getCount() == 0)
            no_historical.setVisibility(View.VISIBLE);
        else
            no_historical.setVisibility(View.GONE);
    }

    private void readDataFromDatabase(){
        Query myTopPostsQuery = mRef.orderByChild("time");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v("test",""+ childDataSnapshot.getKey()); //displays the key for the node
                    Log.v("value",""+ childDataSnapshot.child("creationDate").getValue());   //gives the value for given keyname
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
