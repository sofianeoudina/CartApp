package com.example.sofiane.esiee_drive.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.sofiane.esiee_drive.Classes.Directory;
import com.example.sofiane.esiee_drive.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by severin on 22/02/2017.
 */

public class Fragment_Subject extends ListFragment {

    FirebaseDatabase mDataBase;
    // Create a storage reference from our app
    DatabaseReference mDataRef;
    String yearName;
    String subjectFolder = "subject";
    OnSubjectSetName mSubjectName;

    public interface OnSubjectSetName{
        public void sendSubjectName(String yearFolder, String yearName, String subjectFolder, String subjectName);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.w(TAG, activity.toString());
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try{
            mSubjectName = (Fragment_Subject.OnSubjectSetName)getActivity();
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implements OnFolderSetName");
        }
    }

    public void sendSubject(){
        mSubjectName.sendSubjectName("", yearName, "", "");
        System.out.println(yearName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance();//accessing your storage bucket is to create an instance of FirebaseStorage
        mDataRef = mDataBase.getReference(); // Create a storage reference from our app
    }

    public static Fragment_Subject newInstance(String yearName) {
        Bundle bundle = new Bundle();
        bundle.putString("yearName", yearName);
        Fragment_Subject fragment = new Fragment_Subject();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.your layout filename for each of your fragments
        yearName = getArguments().getString("yearName");
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }

    EditText fName;
    FloatingActionButton addButton = null;
    ArrayList<String> items;
    ArrayAdapter<String> folderAdapter;
    ListView lv = null; //La liste qui contient les répertoires

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Matières");

        addButton = (FloatingActionButton) view.findViewById(R.id.button_add);
        lv = (ListView) view.findViewById(android.R.id.list);


        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSubject();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, Fragment_file.newInstance(yearName, items.get(position)));
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                Log.e("long clicked", "pos: " + pos);

                return true;
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        items = new ArrayList<String>();//Création de la liste des répertoires des années
        items.clear();
        Query recentPostsQuery = null;

        Log.w("onActivityCreated", yearName);
        recentPostsQuery = mDataRef.child("directories").child(yearName).child("subject").orderByChild("folderName");



        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Log.d(TAG, "items:" + items.size());
                items.add(dataSnapshot.getKey());
                folderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };

        recentPostsQuery.addChildEventListener(childEventListener);



        folderAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, items);


        setListAdapter(folderAdapter);

    }

    /*
    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }*/

}

