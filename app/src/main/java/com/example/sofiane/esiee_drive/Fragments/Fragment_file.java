package com.example.sofiane.esiee_drive.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sofiane.esiee_drive.Classes.Directory;
import com.example.sofiane.esiee_drive.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by severin on 25/02/2017.
 */

public class Fragment_file extends ListFragment {

    private static final int PICK_FILE_REQUEST = 234; //a constant to track the file chooser intent

    FirebaseDatabase mDataBase;
    // Create a storage reference from our app
    DatabaseReference mDataRef;
    String yearName = "";
    String subjectName = "";
    private ArrayList<String> items;
    private ArrayAdapter<String> folderAdapter;
    private ListView lv = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mDataBase = FirebaseDatabase.getInstance();//accessing your storage bucket is to create an instance of FirebaseStorage
        mDataRef = mDataBase.getReference(); // Create a storage reference from our app
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.your layout filename for each of your fragments
        yearName = getArguments().getString("yearName");
        subjectName = getArguments().getString("subjectName");
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

    public static Fragment_file newInstance(String yearName, String subjectName) {
        Bundle bundle = new Bundle();
        bundle.putString("yearName", yearName);
        bundle.putString("subjectName", subjectName);

        Fragment_file fragment = new Fragment_file();
        fragment.setArguments(bundle);
        return fragment;

    }

    FloatingActionButton uploadButton = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Cours");

        uploadButton = (FloatingActionButton) view.findViewById(R.id.button_upload);


        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w(TAG, "Upload");
                showFileChooser();
            }
        });

        lv = (ListView) view.findViewById(android.R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, Fragment_display_file.newInstance(yearName + "/" + subjectName + "/" + items.get(position)));
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    //method to show file chooser
    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            System.out.println("" + filePath);
            String folder = yearName + "/" + subjectName + "/" + filePath.getLastPathSegment();
            uploadFile(filePath, folder);

        }
    }

    private void uploadFile(final Uri filePath, String directoryPath) {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference riversRef = storage.getReferenceFromUrl("gs://schooldrive-459b1.appspot.com").child(directoryPath);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying a success toast
                            Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            writeNewDirectory(yearName, subjectName, filePath.getLastPathSegment());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {

            System.out.print("Erreur le chemin n'est pas correct");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        items = new ArrayList<String>();//Création de la liste des répertoires des années
        items.clear();
        Query recentPostsQuery = null;

        Log.w("onActivityCreated", yearName);
        recentPostsQuery = mDataRef.child("directories").child(yearName).child("subject").child(subjectName).child("course").orderByChild("name");


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
                folderAdapter.notifyDataSetChanged();

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();
                folderAdapter.notifyDataSetChanged();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                String commentKey = dataSnapshot.getKey();
                folderAdapter.notifyDataSetChanged();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                folderAdapter.notifyDataSetChanged();
            }
        };

        recentPostsQuery.addChildEventListener(childEventListener);

        folderAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, items);


        setListAdapter(folderAdapter);


    }

    private void writeNewDirectory(String yearName, String subjectName, String fileName) {

        String name = fileName;
        mDataRef.child("directories").child(yearName).child("subject").child(subjectName).child("course").child(name).setValue(name);
    }

    public void onBackPressed() {
        getActivity().onBackPressed();
        if(folderAdapter!=null)
            folderAdapter.notifyDataSetChanged();
    }
}
