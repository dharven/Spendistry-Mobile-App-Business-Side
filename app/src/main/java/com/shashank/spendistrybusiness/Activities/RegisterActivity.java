package com.shashank.spendistrybusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstName, lastName, email, password, confirmPassword, phone, address, city, state, businessName, tollFree, website, panNumber, gstNumber;
    private Button registerButton;
    private String firstNameString, lastNameString, emailString, passwordString, confirmPasswordString, phoneString, addressString, cityString, stateString, businessNameString, tollFreeString, websiteString, panNumberString, gstNumberString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setStrings();
                    if (firstNameString.equals("")) {
                        firstName.setError("First Name is required");
                    } else if (lastNameString.equals("")) {
                        lastName.setError("Last Name is required");
                    } else if (emailString.equals("")) {
                        email.setError("Email is required");
                    } else if (passwordString.equals("")) {
                        password.setError("Password is required");
                    } else if (confirmPasswordString.equals("")) {
                        confirmPassword.setError("Confirm Password is required");
                    } else if (phoneString.equals("")) {
                        phone.setError("Phone is required");
                    } else if (addressString.equals("")) {
                        address.setError("Address is required");
                    } else if (cityString.equals("")) {
                        city.setError("City is required");
                    } else if (stateString.equals("")) {
                        state.setError("State is required");
                    } else if (businessNameString.equals("")) {
                        businessName.setError("Business Name is required");
                    } else {
                        if (passwordString.equals(confirmPasswordString)) {
                           authViewModel.createAccount(emailString, passwordString).observe(RegisterActivity.this, new Observer<Auth>() {
                               @Override
                               public void onChanged(Auth auth) {
                                   if (!auth.getEmail().equals("")) {
                                       authViewModel.CreateInventory(emailString);
                                       authViewModel.createVendorData(new Vendor(firstNameString, lastNameString, emailString, businessNameString,
                                               phoneString, panNumberString, gstNumberString, addressString,
                                               cityString, stateString, tollFreeString, websiteString)).observe(RegisterActivity.this, new Observer<Vendor>() {
                                           @Override
                                           public void onChanged(Vendor vendor) {
                                               Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                               startActivity(intent);
                                           }
                                       });
                                   }
                               }
                           });
                        }
                    }
            }
        });

    }

    private void setStrings() {
        firstNameString = firstName.getText().toString();
        lastNameString = lastName.getText().toString();
        emailString = email.getText().toString();
        passwordString = password.getText().toString();
        confirmPasswordString = confirmPassword.getText().toString();
        phoneString = phone.getText().toString();
        addressString = address.getText().toString();
        cityString = city.getText().toString();
        stateString = state.getText().toString();
        businessNameString = businessName.getText().toString();
        tollFreeString = tollFree.getText().toString();
        websiteString = website.getText().toString();
        panNumberString = panNumber.getText().toString();
        gstNumberString = gstNumber.getText().toString();
    }


    private void findViews(){
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        phone = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        businessName = findViewById(R.id.business_name);
        tollFree = findViewById(R.id.toll_free);
        website = findViewById(R.id.website);
        panNumber = findViewById(R.id.pan_number);
        gstNumber = findViewById(R.id.gst_number);
        registerButton = findViewById(R.id.reg_btn);
    }
}