package com.example.sofiane.esiee_drive.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofiane.esiee_drive.Classes.User;
import com.example.sofiane.esiee_drive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

/**
 * Created by Sofiane on 18/01/2017.
 */

public class editUserInfoFragment extends Fragment {
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private User user;
    private EditText et_name;
    private EditText et_family_name;
    private Button b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_info_user, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_name = (EditText) view.findViewById(R.id.edit_name);
        et_family_name = (EditText) view.findViewById(R.id.edit_family_name);
        b = (Button) view.findViewById(R.id.button_edit_info_user);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String family_name = et_family_name.getText().toString();
                if(validateForm(name, family_name)){
                    storeDataInDatabase(name, family_name);
                    Toast.makeText(getActivity().getApplicationContext(),R.string.data_stored, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),R.string.name_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm(String name, String family_name){
        String namePattern = "^[a-zA-Z]+$";
        return Pattern.matches(namePattern, name) && Pattern.matches(namePattern, family_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        FirebaseUser userFromFirebase = mAuth.getCurrentUser();
        String userId = userFromFirebase.getUid();

        mRef = database.getReference().child("users").child(userId);

        readDataFromDatabase(userId);

    }
    private void readDataFromDatabase(final String userId){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String username = String.valueOf(dataSnapshot.child("user_name").getValue());
                String name = String.valueOf(dataSnapshot.child("user_family_name").getValue());
                String mail = String.valueOf(dataSnapshot.child("email").getValue());

                user = new User(username, name, mail);
                user.setUser_id(userId);

                et_name.setText(user.getUser_name());
                et_family_name.setText(user.getUser_family_name());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void storeDataInDatabase(String name, String family_name){
        
    }
}
