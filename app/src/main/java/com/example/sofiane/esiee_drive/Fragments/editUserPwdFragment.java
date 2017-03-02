package com.example.sofiane.esiee_drive.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sofiane.esiee_drive.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

/**
 * Created by Sofiane on 18/01/2017.
 */

public class editUserPwdFragment extends Fragment {
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    FirebaseUser userFromFirebase;
    private DatabaseReference mRef;
    private static final String TAG = "Update user's mail";

    private EditText edit_new_pwd;
    private EditText edit_confirm_new_pwd;
    private Button validate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_pwd_user, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edit_new_pwd = (EditText) view.findViewById(R.id.edit_pwd);
        edit_confirm_new_pwd = (EditText) view.findViewById(R.id.edit_confirm_pwd);
        validate = (Button) view.findViewById(R.id.button_edit_pwd);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_pwd = edit_new_pwd.getText().toString();
                String confirm_new_pwd = edit_confirm_new_pwd.getText().toString();
                if(validateForm(new_pwd, confirm_new_pwd)){
                    updateDataInDatabase(new_pwd);
                    Toast.makeText(getActivity().getApplicationContext(),R.string.data_stored, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),R.string.name_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateForm(String new_pwd, String confirm_new_pwd){
        return new_pwd.equals(confirm_new_pwd) && new_pwd.length() > 6;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        userFromFirebase = mAuth.getCurrentUser();
    }

    private void updateDataInDatabase(String new_pwd){
        userFromFirebase.updatePassword(new_pwd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        edit_new_pwd.setText("");
                        edit_confirm_new_pwd.setText("");
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }
}
