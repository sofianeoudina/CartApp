package com.example.sofiane.esiee_drive.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sofiane.esiee_drive.Classes.Directory;
import com.example.sofiane.esiee_drive.R;
import com.example.sofiane.esiee_drive.folder_layout;
import com.example.sofiane.esiee_drive.home_page;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Sofiane on 16/01/2017.
 */


public class Fragment_folder extends ListFragment {

    private FirebaseDatabase mDataBase;
    // Create a storage reference from our app
    private DatabaseReference mDataRef;
    private String yearFolder = "directories";
    private OnFolderSetName mFolderName;

    private EditText fName;
    private FloatingActionButton addButton = null;
    private ArrayList<String>items;
    private ArrayAdapter<String> folderAdapter;
    private ListView lv = null; //La liste qui contient les répertoires
    private int currentFolder;

    public interface OnFolderSetName{
        public void onSendFolderName(String yearFolder, String yearName, String subjectFolder, String subjectName);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.w(TAG, activity.toString());
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try{
            mFolderName = (OnFolderSetName)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implements OnFolderSetName");
        }
    }

    public void sendName()
    {
        mFolderName.onSendFolderName("directories", "", "", "");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBase = FirebaseDatabase.getInstance();//accessing your storage bucket is to create an instance of FirebaseStorage
        mDataRef = mDataBase.getReference(); // Create a storage reference from our app

        items = new ArrayList<String>();//Création de la liste des répertoires des années

        Query recentPostsQuery = null;

        recentPostsQuery = mDataRef.child(yearFolder).orderByChild("folderName");



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


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.your layout filename for each of your fragments

        return inflater.inflate(R.layout.fragment_folder, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dossiers");

        addButton = (FloatingActionButton) view.findViewById(R.id.button_add);
        lv = (ListView) view.findViewById(android.R.id.list);

        addButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        sendName();
                    }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearFolder = items.get(position);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, Fragment_Subject.newInstance(yearFolder), "fragment_Subject");
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

        Collections.sort(items, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });


        registerForContextMenu(getListView());

        final ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                currentFolder = position;
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.a_item:
                        removeFolder(items.get(currentFolder));
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    case R.id.b_item:
                        Log.w(TAG,"a"+currentFolder);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_contextuel, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });



    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }



    public void removeFolder(String folderName) {
        mDataRef.child("directories").child(folderName).removeValue();
        items.remove(folderName);
        folderAdapter.notifyDataSetChanged();//Actualise l'adapter
    }




        /*Upload from a stream*/


    /*
    public void updateDirectory(File folder)
    {
        //check for permission
        if(ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
        //ask for permission
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        }

        //On change le titre de l'activité

        //On change le répertoire actuel
        mCurrentFolder = folder;

        //On vide les répertoires actuels
        setEmpty();

        //On récupère la liste des fichiers du répertoire courant
        File[] files = m.CurrentFolder.listFiles();

        if(files != null){
            for(File f : files)
                folderAdapter.add(f);

            //On trie la liste de fichiers
            mAdapter.sort();
        }
    }


    public void setEmpty()
    {
        //Si l'adaptater n'est pas vide
        if(!folderAdapter.isEmpty())
            folderAdapter.clear();//On le vide
    }

    public void displayFileContent(File file)
    {
        String extension = file.getName().substring(file.getName.indexOf("." +1).toLOwerCase();
        if(extension.)
    }*/

}
