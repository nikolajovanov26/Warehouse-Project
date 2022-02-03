package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    TextView email,password;
    Button login;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);

        login = findViewById(R.id.login);
        login.setOnClickListener(this);

        register = findViewById(R.id.SwitchRegister);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SwitchRegister:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        String email_db = email.getText().toString().trim();
        String password_db = password.getText().toString().trim();
        //Toast.makeText(MainActivity.this, email_db+ " " +password_db, Toast.LENGTH_SHORT).show();


        if(email_db.isEmpty()){
            email.setError("Enter Email Address");
            email.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(email_db).matches()){
            email.setError("Enter a Valid Email Address");
            email.requestFocus();
            return;
        }

        if(password_db.isEmpty()){
            password.setError("Enter Password");
            password.requestFocus();
            return;
        }

        if(password_db.length() < 6){
            password.setError("Your Password must be at least 6 characters");
            password.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email_db,password_db).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user;
                    DatabaseReference reference;
                    String userID;

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    userID = user.getUid();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                            User userProfil = snapshot.getValue(User.class);


                            if(userProfil != null){
                                String tip = userProfil.type;
                                if(tip.equals("Warehouse Manager")){
                                    startActivity(new Intent(MainActivity.this, MainWarehouse.class));
                                }
                                else if(tip.equals("Driver")){
                                    startActivity(new Intent(MainActivity.this, MainDriver.class));
                                }
                                else if(tip.equals("Store Manager")){
                                    startActivity(new Intent(MainActivity.this, MainStore.class));
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Failed to login: " + tip, Toast.LENGTH_SHORT).show();
                                }

                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });


                } else {
                    Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}