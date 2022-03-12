package com.shashank.spendistrybusiness.Activities;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstName, lastName, email, password, confirmPassword, phone, address, city, state, businessName, tollFree, website, panNumber, gstNumber;
    private Button registerButton;
    private String firstNameString, lastNameString, emailString, passwordString, confirmPasswordString, phoneString, addressString, cityString, stateString, businessNameString, tollFreeString, websiteString, panNumberString, gstNumberString;
    private TextInputLayout firstNameField, lastNameField, emailField, passwordField, confirmPasswordField, phoneField, addressField, cityField, stateField, businessNameField, tollFreeField, websiteField, panNumberField, gstNumberField;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();

//        Window window = getWindow();
//        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.windowBlue));
//        window.setStatusBarColor(ContextCompat.getColor(this,R.color.windowBlue));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
//        } else {
//            window.setStatusBarColor(ContextCompat.getColor(this,R.color.cardBlue));
//        }
        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.cardBlue));
        window.setBackgroundDrawableResource(R.color.cardBlue);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.cardBlue));


        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStrings();
                if (firstNameString.equals("")) {
                    firstNameField.setError("First Name is required");
                    scrollView.smoothScrollTo(0, firstNameField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            firstNameField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (lastNameString.equals("")) {
                    lastNameField.setError("Last Name is required");
                    scrollView.smoothScrollTo(0, lastNameField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lastNameField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (emailString.equals("")) {
                    emailField.setError("Email is required");
                    scrollView.smoothScrollTo(0, emailField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            emailField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (passwordString.equals("")) {
                    passwordField.setError("Password is required");
                    scrollView.smoothScrollTo(0, passwordField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (confirmPasswordString.equals("")) {
                    confirmPasswordField.setError("Confirm Password is required");
                    scrollView.smoothScrollTo(0, confirmPasswordField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            confirmPasswordField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (phoneString.equals("")) {
                    phoneField.setError("Phone is required");
                    scrollView.smoothScrollTo(0, phoneField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            phoneField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (addressString.equals("")) {
                    addressField.setError("Address is required");
                    scrollView.smoothScrollTo(0, addressField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addressField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (cityString.equals("")) {
                    cityField.setError("City is required");
                    scrollView.smoothScrollTo(0, cityField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cityField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (stateString.equals("")) {
                    stateField.setError("State is required");
                    scrollView.smoothScrollTo(0, stateField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stateField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if (businessNameString.equals("")) {
                    businessNameField.setError("Business Name is required");
                    scrollView.smoothScrollTo(0, businessNameField.getTop());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            businessNameField.setErrorEnabled(false);
                        }
                    }, 1000);

                } else if(registerButton.getText().toString().equals("Next")) {
                     scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    registerButton.setText("Register");
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
//if scrollview is at bottom button text automatically changes to register
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getScrollY() == scrollView.getChildAt(0).getHeight() - scrollView.getHeight()) {
                    registerButton.setText("Register");
                } else {
                    registerButton.setText("Next");
                }
            }
                                                                    });

        scrollView.setSmoothScrollingEnabled(true);

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


    private void findViews() {
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
        //
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passField);
        confirmPasswordField = findViewById(R.id.confirmPassField);
        phoneField = findViewById(R.id.phoneField);
        addressField = findViewById(R.id.addressField);
        cityField = findViewById(R.id.cityField);
        stateField = findViewById(R.id.stateField);
        businessNameField = findViewById(R.id.businessNameField);
        tollFreeField = findViewById(R.id.tollFreeField);
        websiteField = findViewById(R.id.websiteField);
        panNumberField = findViewById(R.id.panField);
        gstNumberField = findViewById(R.id.gstField);
        //
        scrollView = findViewById(R.id.scrollView);

    }
}