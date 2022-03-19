package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName, lastName, phone, address, city, state, businessName, tollFree, website, panNumber, gstNumber, description;
    private Button update, change_pass, Add_location;
    private String firstNameString, lastNameString, emailString, phoneString, addressString, cityString, stateString, businessNameString, tollFreeString, websiteString, panNumberString, gstNumberString, descriptionString;
    private TextInputLayout firstNameField, lastNameField, emailField, passwordField, confirmPasswordField, phoneField, addressField, cityField, stateField, businessNameField, tollFreeField, websiteField, panNumberField, gstNumberField;
    private ScrollView scrollView;
    private RoundedImageView profileImage;
    private TextView email;
    private Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Window window = this.getWindow();
        window.setNavigationBarColor(this.getResources().getColor(com.google.zxing.client.android.R.color.zxing_transparent));
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        getViewsById();
        Intent intent = getIntent();
        vendor = intent.getParcelableExtra("vendorDetails");
        setData(vendor);
        RelativeLayout layout = findViewById(R.id.profile_layout);

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImage.showContextMenu();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStringData();
                if(firstNameString.equals("") || lastNameString.equals("") || phoneString.equals("") ||
                        addressString.equals("") || cityString.equals("") || stateString.equals("") ||
                        businessNameString.equals("")  || panNumberString.equals("")){
                    if(firstNameString.equals("")){
                        firstNameField.setError("First Name is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firstNameField.setErrorEnabled(false);
                            }
                        },1500);
                    } else if(lastNameString.equals("")){
                        lastNameField.setError("Last Name is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lastNameField.setErrorEnabled(false);
                            }
                        },1500);
                    } else if(phoneString.equals("")){
                        phoneField.setError("Phone Number is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                phoneField.setErrorEnabled(false);

                            }
                        },1500);

                    } else if(addressString.equals("")){
                        addressField.setError("Address is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addressField.setErrorEnabled(false);

                            }
                        },1500);
                    } else if(cityString.equals("")){
                        cityField.setError("City is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cityField.setErrorEnabled(false);

                            }
                        },1500);
                    } else if(stateString.equals("")){
                        stateField.setError("State is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stateField.setErrorEnabled(false);
                            }
                        },1500);
                    } else if(businessNameString.equals("")){
                        businessNameField.setError("Business Name is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                businessNameField.setErrorEnabled(false);
                            }
                        },1500);
                    } else if(panNumberString.equals("")){
                        panNumberField.setError("Pan Number is required");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                panNumberField.setErrorEnabled(false);
                            }
                        },1500);
                    } else if(phoneString.length() != 10){
                        phoneField.setError("Phone Number must be 10 digits");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                phoneField.setErrorEnabled(false);
                            }
                        },1500);
                    } else {
                        Vendor vendor = new Vendor(firstNameString, lastNameString,emailString, businessNameString, phoneString,panNumberString, gstNumberString, addressString, cityString, stateString, tollFreeString, websiteString, descriptionString);
                        authViewModel.updateProfile(layout,emailString,vendor).observe(EditProfileActivity.this, new Observer<Vendor>() {
                            @Override
                            public void onChanged(Vendor vendor) {
                                setData(vendor);
                            }
                        });
                    }
                }
            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(EditProfileActivity.this, OtpActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
               intent.putExtra("email",sharedPreferences.getString("email", ""));
              startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(Vendor vendor) {
        email.setText("Email: "+vendor.getEmail());
        firstName.setText(vendor.getFirstName());
        lastName.setText(vendor.getLastName());
        phone.setText(vendor.getMobileNumber());
        address.setText(vendor.getAddress());
        city.setText(vendor.getCity());
        state.setText(vendor.getState());
        businessName.setText(vendor.getBusinessName());
        tollFree.setText(vendor.getTollFreeNumber());
        website.setText(vendor.getWebsite());
        panNumber.setText(vendor.getPanNumber());
        gstNumber.setText(vendor.getGstNumber());
        description.setText(vendor.getDescription());
    }

    private void getStringData(){
        emailString = email.getText().toString();
        firstNameString = firstName.getText().toString();
        lastNameString = lastName.getText().toString();
        phoneString = phone.getText().toString();
        addressString = address.getText().toString();
        cityString = city.getText().toString();
        stateString = state.getText().toString();
        businessNameString = businessName.getText().toString();
        tollFreeString = tollFree.getText().toString();
        websiteString = website.getText().toString();
        panNumberString = panNumber.getText().toString();
        gstNumberString = gstNumber.getText().toString();
        descriptionString = description.getText().toString();

    }

    private void getViewsById() {
        firstName = findViewById(R.id.first_name_profile);
        lastName = findViewById(R.id.last_name_profile);
        businessName = findViewById(R.id.business_name_profile);
        phone = findViewById(R.id.phone_number_profile);
        address = findViewById(R.id.address_profile);
        city = findViewById(R.id.city_profile);
        state = findViewById(R.id.state_profile);
        tollFree = findViewById(R.id.tollFree_profile);
        website = findViewById(R.id.website_profile);
        panNumber = findViewById(R.id.pan_number_profile);
        gstNumber = findViewById(R.id.gst_number_profile);
        description = findViewById(R.id.description_profile);
        email = findViewById(R.id.email_profile);

        //TextInputLayout
        firstNameField = findViewById(R.id.firstNameField_profile);
        lastNameField = findViewById(R.id.lastNameField_profile);
        phoneField = findViewById(R.id.phoneField_profile);
        addressField = findViewById(R.id.addressField_profile);
        cityField = findViewById(R.id.cityField_profile);
        stateField = findViewById(R.id.stateField_profile);
        businessNameField = findViewById(R.id.businessNameField_profile);
        tollFreeField = findViewById(R.id.tollFreeField_profile);
        websiteField = findViewById(R.id.websiteField_profile);
        panNumberField = findViewById(R.id.panField_profile);
        gstNumberField = findViewById(R.id.gstField_profile);

        //scrollView
        scrollView = findViewById(R.id.scrollView_profile);

        //Button
        update = findViewById(R.id.update_profile);
        change_pass = findViewById(R.id.change_pass);
        Add_location = findViewById(R.id.location_add);

        //Image View
        profileImage = findViewById(R.id.profile_image_view);
        registerForContextMenu(profileImage);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        menu.setHeaderTitle(R.string.context_title);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                return true;
            case R.id.remove_pic:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}