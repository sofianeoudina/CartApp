package com.example.sofiane.esiee_drive.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sofiane.esiee_drive.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by severin on 26/02/2017.
 */

public class Fragment_display_file extends Fragment {

    String directoryPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.your layout filename for each of your fragments
        directoryPath = getArguments().getString("directoryPath");
        return inflater.inflate(R.layout.fragment_display_file, container, false);
    }

    public static Fragment_display_file newInstance(String directoryPath) {
        Bundle bundle = new Bundle();
        bundle.putString("directoryPath", directoryPath);

        Fragment_display_file fragment = new Fragment_display_file();
        fragment.setArguments(bundle);
        return fragment;

    }
    ImageView mImageView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("File");

        mImageView = (ImageView) view.findViewById(R.id.file_view);

        displayFile(directoryPath);
    }


    public void displayFile(String directoryPath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference riversRef = storage.getReferenceFromUrl("gs://schooldrive-459b1.appspot.com").child(directoryPath);

        try {
            final File localFile = File.createTempFile("images", "jpg");

            riversRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = new BitmapFactory().decodeFile(localFile.getAbsolutePath());
                    mImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }catch (IOException e){
        e.printStackTrace();
    }
    }
}
