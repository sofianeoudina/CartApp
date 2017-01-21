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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "AnonymousAuth";

    private EditText EmailField;
    private EditText PasswordField;
    private Button connectButton;
    private TextView createAccount;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        EmailField = (EditText) findViewById(R.id.EmailField);
        PasswordField = (EditText) findViewById(R.id.PasswordField);

        connectButton = (Button) findViewById(R.id.connectButton);

        createAccount = (TextView) findViewById(R.id.CreateAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateUser.class);
                startActivity(intent);
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(EmailField.getText().toString(), PasswordField.getText().toString());
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goToMainPage();

                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void SignIn(String email, String password){
        if (!validateForm(email, password)) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(MainActivity.this, R.string.connectionSuccess, Toast.LENGTH_SHORT).show();
                            goToMainPage();
                        }


                    }
                });
    }
    public boolean validateForm(String email, String password){
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$";
        boolean isEmailValide = Pattern.matches(emailPattern, email);
        boolean isPasswordValide = !password.isEmpty() && password.length() > 6;

        if (!isEmailValide)
        {
            Toast.makeText(getApplicationContext(),R.string.email_invalide, Toast.LENGTH_SHORT).show();
            EmailField.setError("Erreur");
        }
        if (!isPasswordValide)
        {
            Toast.makeText(getApplicationContext(),R.string.no_password, Toast.LENGTH_SHORT).show();
            PasswordField.setError("Erreur");
        }

        return isEmailValide && isPasswordValide;
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public void goToMainPage(){
        //Intent intent = new Intent(MainActivity.this, MainPage.class);
        Intent intent = new Intent(MainActivity.this, home_page.class);
        startActivity(intent);
        finish();
    }
}
