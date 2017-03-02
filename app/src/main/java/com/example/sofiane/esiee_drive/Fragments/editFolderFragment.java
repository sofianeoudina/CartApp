package com.example.sofiane.esiee_drive.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sofiane.esiee_drive.Classes.Directory;
import com.example.sofiane.esiee_drive.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by severin on 18/02/2017.
 */
public class editFolderFragment extends DialogFragment {

    DatabaseReference mDatabase;
    // Create a storage reference from our app
    StorageReference storageRef;
    String yearFolder = "directories";
    String yearName = "";
    String subjectFolder = "subject";
    String subjectName = "";

    private String title;
    private String message;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.w(TAG, activity.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public static editFolderFragment newInstance(String title, String message){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);


        editFolderFragment fragment = new editFolderFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void receiveFolderName(String yearFolder)
    {
        this.yearFolder = yearFolder;
        Log.w(TAG, yearFolder);
    }

    public void receiveYearName(String yearName)
    {
        this.yearName = yearName;
        Log.w(TAG, yearName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.title = getArguments().getString("title");
        this.message = getArguments().getString("message");
        return inflater.inflate(R.layout.fragment_edit_folder, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        Button valid = (Button) view.findViewById(R.id.button_validate);
        Button cancel = (Button) view.findViewById(R.id.button_cancel);
        final EditText edt = (EditText) view.findViewById(R.id.edit_folder);
        final TextView tv = (TextView) view.findViewById(android.R.id.title);

        tv.setText(title);
        edt.setHint(message);

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subjectName = edt.getText().toString();

                // Create a storage reference from our app
                if(yearName == "") {
                    writeNewDirectory(edt.getText().toString(), "", edt.getText().toString(), new Date(System.currentTimeMillis()));
                }
                else {
                    Log.i("dialogue", "click");
                    writeNewDirectory(yearName, subjectName, edt.getText().toString(), new Date(System.currentTimeMillis()));
                }
                dismiss();
            }
        });

       cancel.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });

    }

    private void writeNewDirectory(String yearName, String subjectName, String folderName, Date creationDate) {

        Directory directory = new Directory(folderName, creationDate);

        if(subjectName == "") {

            mDatabase.child(yearFolder).child(yearName).setValue(directory);
        }

        else{
            mDatabase.child(yearFolder).child(yearName).child(subjectFolder).child(subjectName).setValue(directory);
        }
    }



}
