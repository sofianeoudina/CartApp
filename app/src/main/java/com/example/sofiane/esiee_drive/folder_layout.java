package com.example.sofiane.esiee_drive;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.sofiane.esiee_drive.Fragments.Fragment_folder;
import com.example.sofiane.esiee_drive.Fragments.editFolderFragment;

public class folder_layout extends AppCompatActivity implements Fragment_folder.OnFolderSetName {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_layout);
        getSupportActionBar().hide();
        System.out.println("folder_layout");

    }

    @Override
    public void onSendFolderName(String yearFolder, String yearName, String subjectFolder, String subjectName) {
        editFolderFragment editFolderFragment = (editFolderFragment)getSupportFragmentManager().findFragmentByTag("dialog");

        if (editFolderFragment != null){
            Log.w("folder", yearFolder);
            editFolderFragment.receiveFolderName(yearFolder);
        }else{

            editFolderFragment fragment = new editFolderFragment();

            Bundle bundle = new Bundle();
            bundle.putString("folderName", yearFolder);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}