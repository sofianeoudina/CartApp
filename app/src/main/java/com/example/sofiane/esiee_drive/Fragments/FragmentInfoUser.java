package com.example.sofiane.esiee_drive.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofiane.esiee_drive.Classes.User;
import com.example.sofiane.esiee_drive.MainActivity;
import com.example.sofiane.esiee_drive.R;
import com.example.sofiane.esiee_drive.home_page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sofiane on 16/01/2017.
 */

public class FragmentInfoUser extends Fragment{
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.fragment_1, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(R.string.info_user);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        FirebaseUser userFromFirebase = mAuth.getCurrentUser();
        String userId = userFromFirebase.getUid();

        mRef = database.getReference().child("users").child(userId);

        readDataFromDatabase();

    }

    private void readDataFromDatabase(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                TextView tv_family_name = (TextView) view.findViewById(R.id.text_family_name);
                TextView tv_email = (TextView) view.findViewById(R.id.text_mail);

                String username = String.valueOf(dataSnapshot.child("user_name").getValue());
                String name = String.valueOf(dataSnapshot.child("user_family_name").getValue());
                String mail = String.valueOf(dataSnapshot.child("email").getValue());
                //User user = (User) dataSnapshot.getValue();
                tv_family_name.setText(name + " " + username);
                tv_email.setText(mail);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }
}
