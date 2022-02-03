package com.example.warehouseproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Address;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView login;
    Button locationBtn, register;
    TextView locationText, name, phone, email, password;
    LinearLayout locatonLL;
    int PLACE_PICKER_REQUEST=1;
    Double lat,lon;
    String location, type;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.SwitchLogin);
        login.setOnClickListener(this);

        name = findViewById(R.id.registerName);
        phone = findViewById(R.id.registerPhone);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);

        locatonLL = findViewById(R.id.location);
        locationBtn = findViewById(R.id.registerLocationSelect);
        locationBtn.setOnClickListener(this);
        locationText = findViewById(R.id.registerLocationText);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        Spinner userTypes = findViewById(R.id.userType);
        String userType = userTypes.getSelectedItem().toString();

        userTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String userType = userTypes.getSelectedItem().toString();
                switch (userType){
                    case "Warehouse Manager":
                        locatonLL.setVisibility(View.VISIBLE);
                        type = "Warehouse Manager";
                        break;
                    case "Driver":
                        locatonLL.setVisibility(View.GONE);
                        locationText.setText("");
                        type = "Driver";
                        break;
                    case "Store Manager":
                        locatonLL.setVisibility(View.VISIBLE);
                        type = "Store Manager";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SwitchLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerLocationSelect:

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(RegisterUser.this)
                            ,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                lat = Double.valueOf(place.getLatLng().latitude);
                lon = Double.valueOf(place.getLatLng().longitude);

                getAddress(lat, lon);

                locationText.setText(location);
            }

        }
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            location = add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void registerUser(){
        String name_db = name.getText().toString().trim();
        String phone_db = phone.getText().toString().trim();
        String email_db = email.getText().toString().trim();
        String password_db = password.getText().toString().trim();
        String type_db = type;
        String location_db = locationText.getText().toString().trim();
        Double lat_db = lat;
        Double lon_db = lon;

        if(name_db.isEmpty()){
            name.setError("Enter Name");
            name.requestFocus();
            return;
        }

        if(phone_db.isEmpty()){
            phone.setError("Enter Phone Number");
            phone.requestFocus();
            return;
        }

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

        if(!type_db.equals("Driver")){
            if(location_db.isEmpty()){
                locationText.setError("Select Location");
                locationText.requestFocus();
                return;
            }
        }




        mAuth.createUserWithEmailAndPassword(email_db, password_db)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User();

                            if(type_db.equals("Driver")){
                                user = new User (type_db, name_db, phone_db, email_db, password_db);
                            } else {
                                user = new User (type_db, name_db, phone_db, email_db, password_db, location_db, lat_db, lon_db);
                            }

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "You have been registered", Toast.LENGTH_SHORT).show();
                                        najava();

                                    } else {
                                        Toast.makeText(RegisterUser.this, "A problem has occurred", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, "Problem has occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void najava(){

        if(type.equals("Warehouse Manager")){
            startActivity(new Intent(RegisterUser.this, MainWarehouse.class));
        } else if(type.equals("Driver")){
            startActivity(new Intent(RegisterUser.this, MainDriver.class));
        } else if(type.equals("Store Manager")){
        startActivity(new Intent(RegisterUser.this, MainStore.class));
        } else {
            Toast.makeText(RegisterUser.this, "Problem logging in", Toast.LENGTH_SHORT).show();
        }


    }
}