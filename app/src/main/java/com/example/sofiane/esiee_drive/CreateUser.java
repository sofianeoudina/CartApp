package com.example.sofiane.esiee_drive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sofiane.esiee_drive.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class CreateUser extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Create Account";

    private EditText NameField;
    private EditText FamilyNameField;
    private EditText EmailField;
    private EditText PasswordField;
    private EditText ConfirmationPasswordField;
    private ProgressBar progressBar;

    private Button ValidateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        NameField = (EditText) findViewById(R.id.NameField);
        FamilyNameField = (EditText) findViewById(R.id.FamilyNameField);
        EmailField = (EditText) findViewById(R.id.EmailField);
        PasswordField = (EditText) findViewById(R.id.PasswordField);
        ConfirmationPasswordField = (EditText) findViewById(R.id.ConfirmationPasswordField);

        ValidateButton = (Button) findViewById(R.id.ValidateButton);

        ValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(EmailField.getText().toString(), PasswordField.getText().toString(),
                        ConfirmationPasswordField.getText().toString());
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    private void createAccount(String email, String password, String confirmationPassword) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(EmailField.getText().toString(), PasswordField.getText().toString(),
                ConfirmationPasswordField.getText().toString())) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //showProgressDialog();
        mAuth.createUserWithEmailAndPassword(EmailField.getText().toString(), PasswordField.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateUser.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            createNewUser(task.getResult().getUser());
                            goToMainPage();
                        }
                    }
                });
    }

    public boolean validateForm(String email, String password, String confirmationPassword){
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.-_]+$";
        boolean isEmailValide = Pattern.matches(emailPattern, email);
        boolean isPasswordValide = password.equals(confirmationPassword) && password.length() > 6;
        boolean isNameValide = !NameField.toString().isEmpty();
        boolean isFamilyNameValide = !FamilyNameField.toString().isEmpty();

        if (!isEmailValide)
        {
            Toast.makeText(getApplicationContext(),R.string.email_invalide, Toast.LENGTH_SHORT).show();
            EmailField.setError("Erreur");
        }
        if (!isPasswordValide)
        {
            Toast.makeText(getApplicationContext(),R.string.password_invalide,Toast.LENGTH_SHORT).show();
            PasswordField.setError("Erreur");
        }

        if (!isNameValide)
        {
            Toast.makeText(getApplicationContext(),R.string.error_name,Toast.LENGTH_SHORT).show();
            PasswordField.setError("Erreur");
        }

        if (!isFamilyNameValide)
        {
            Toast.makeText(getApplicationContext(),R.string.error_familyname,Toast.LENGTH_SHORT).show();
            PasswordField.setError("Erreur");
        }

        return isEmailValide && isPasswordValide && isNameValide && isFamilyNameValide;
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void goToMainPage(){
        startActivity(new Intent(CreateUser.this, MainActivity.class));
        finish();
    }

    private void createNewUser(FirebaseUser userFromRegistration) {
        String username = NameField.getText().toString();
        String familyname = FamilyNameField.getText().toString();
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();

        User user = new User(username,familyname, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}
