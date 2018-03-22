package com.example.android.userapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText passField;
    private EditText emailField;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameField = (EditText) findViewById(R.id.nameField);
        passField = (EditText) findViewById(R.id.passField);
        emailField = (EditText) findViewById(R.id.emailField);
        progressBar = (ProgressBar) findViewById(R.id.register_progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void returnLoginScreenButtonClicked(View view) {
        Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public void registerButtonClicked(View view) {
        final String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String pass = passField.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.animate();
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabaseReference.child(user_id);
                        current_user_db.child("Name").setValue(name);
                        current_user_db.child("Usage").setValue("0");

                        Intent mainIntent = new Intent(SignupActivity.this, LoginActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(SignupActivity.this, "Fill the text fields correctly", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Fill all the text fields to register", Toast.LENGTH_SHORT).show();
        }
    }
}
