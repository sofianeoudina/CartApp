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

public class editUserEmailFragment extends Fragment {
    private View view;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    FirebaseUser userFromFirebase;
    private DatabaseReference mRef;
    private static final String TAG = "Update user's mail";

    private EditText edit_new_mail;
    private EditText edit_confirm_new_mail;
    private Button validate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_user_mail, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edit_new_mail = (EditText) view.findViewById(R.id.edit_mail);
        edit_confirm_new_mail = (EditText) view.findViewById(R.id.edit_confirm_mail);
        validate = (Button) view.findViewById(R.id.button_edit_mail);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_mail = edit_new_mail.getText().toString();
                String confirm_new_mail = edit_confirm_new_mail.getText().toString();
                if(validateForm(new_mail, confirm_new_mail)){
                    updateDataInDatabase(new_mail);
                    Toast.makeText(getActivity().getApplicationContext(),R.string.data_stored, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),R.string.name_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean validateForm(String new_mail, String confirm_new_mail){
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.-_]+$";
        boolean isEmailValide = Pattern.matches(emailPattern, new_mail);
        return isEmailValide && new_mail.equals(confirm_new_mail);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        userFromFirebase = mAuth.getCurrentUser();
        String userId = userFromFirebase.getUid();

        mRef = database.getReference().child("users").child(userId);

    }

    private void updateDataInDatabase(final String mail){
        userFromFirebase.updateEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        update(mail);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Le mail de l'utilisateur à été modifié.");
                        }
                        edit_new_mail.setText("");
                        edit_confirm_new_mail.setText("");
                    }
                });
    }

    private void update(String mail){
        mRef.child("email").setValue(mail);
    }
}
