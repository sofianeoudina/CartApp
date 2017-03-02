package com.example.sofiane.esiee_drive;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.sofiane.esiee_drive.Fragments.FragmentInfoUser;
import com.example.sofiane.esiee_drive.Fragments.Fragment_Subject;
import com.example.sofiane.esiee_drive.Fragments.Fragment_folder;
import com.example.sofiane.esiee_drive.Fragments.Fragment_home;
import com.example.sofiane.esiee_drive.Fragments.editFolderFragment;
import com.example.sofiane.esiee_drive.Fragments.editUserFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Fragment_folder.OnFolderSetName, Fragment_Subject.OnSubjectSetName{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button redirectToFolderLayout;
    private TextView mail_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(user == null){
            goToConnectionPage();
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.
        String uid = user.getUid();

        redirectToFolderLayout = (Button) findViewById(R.id.accessToFolderLayout);
        redirectToFolderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFolderLayout();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        mail_user = (TextView) header.findViewById(R.id.menu_maiL_user);
        mail_user.setText(email);


        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId){
            case R.id.nav_logout:
                signOut();
                break;
            case R.id.info_user:
                fragment = new FragmentInfoUser();
                break;
            case R.id.nav_home:
                fragment = new Fragment_home();
                break;
            case R.id.nav_folder:
                fragment = new Fragment_folder();
                break;
            case R.id.nav_edit_user:
                fragment = new editUserFragment();
                break;
            default:
                fragment = new Fragment_home();
                break;
        }

        //On utilise le FragmentManager pour ajouter à l'activité les fragments de manière dynamique
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void signOut() {
        mAuth.signOut();
        goToConnectionPage();
    }

    private void goToConnectionPage(){
        startActivity(new Intent(home_page.this, MainActivity.class));
        finish();
    }
    private void goToFolderLayout(){
        startActivity(new Intent(home_page.this, folder_layout.class));
        Log.w("start", "folder");
    }

    @Override
    public void onSendFolderName(String yearFolder, String yearName, String subjectFolder, String subjectName) {

        editFolderFragment dialogue = editFolderFragment.newInstance("Nom de promo", "Veuillez saisir le nom de promo");
        dialogue.show(getSupportFragmentManager(), "dialog");
        System.out.println(getSupportFragmentManager().getFragments());
        dialogue.receiveFolderName(yearFolder);
    }

    @Override
    public void sendSubjectName(String yearFolder, String yearName, String subjectFolder, String subjectName) {

        editFolderFragment dialogue = editFolderFragment.newInstance("Nom de la matière", "Veuillez saisir le nom de la matière");
        dialogue.show(getSupportFragmentManager(), "Dialogue");
        dialogue.receiveYearName(yearName);
    }

}
