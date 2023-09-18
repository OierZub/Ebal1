package com.example.ebal1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.ebal1.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static FirebaseAuth mAuth;
    final private static String TAG = "AutentifikazioArduraduna";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // SharedPreferences abiarazi.
        sharedPreferences = getSharedPreferences("LoginDatuak", Context.MODE_PRIVATE);

        // Botoien entzuleak sortu.
        Button bErregistratu = findViewById(R.id.bErregistratu);

        bErregistratu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método registrat aquí
                erregistratu();
            }
        });

        Button bLogeatu = findViewById(R.id.bLogeatu);
        bLogeatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método registrat aquí
                logeatu();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        irakurriLoginDatuak();

    }

    private void irakurriLoginDatuak() {

        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Pasahitza", "");

        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPass = findViewById(R.id.editTextPasahitza);

        etEmail.setText(email);
        etPass.setText(password);

    }


    private void erregistratu() {

        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPass = findViewById(R.id.editTextEmail);

        // Obtener el texto del EditText
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }


    private void logeatu() {

        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPass = findViewById(R.id.editTextEmail);

        // Obtener el texto del EditText
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Gorde Login datuak Shared preferences.
                            datuakGorde();
                            // Deitu ErosketaZerrenda activity-ra.
                            Intent intent = new Intent(MainActivity.this, ErosketaZerrenda.class);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void datuakGorde() {
        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPass = findViewById(R.id.editTextEmail);

        // Obtener el texto del EditText
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", email);
        editor.putString("Pasahitza", password);
        editor.apply();
    }

}