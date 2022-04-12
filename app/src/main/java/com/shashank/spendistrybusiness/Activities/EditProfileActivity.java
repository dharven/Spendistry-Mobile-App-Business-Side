package com.shashank.spendistrybusiness.Activities;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;
import androidx.room.util.FileUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.DialogFragment.AddLocationDialog;
import com.shashank.spendistrybusiness.Models.Auth;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.SpendistryAPI.SpendistryAPI;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity {
    private EditText firstName, lastName, phone, address, city, state, businessName, tollFree, website, panNumber, gstNumber, description;
    private Button update, change_pass, Add_location;
    private String firstNameString, lastNameString, emailString, phoneString, addressString, cityString, stateString, businessNameString, tollFreeString, websiteString, panNumberString, gstNumberString, descriptionString;
    private TextInputLayout firstNameField, lastNameField, emailField, passwordField, confirmPasswordField, phoneField, addressField, cityField, stateField, businessNameField, tollFreeField, websiteField, panNumberField, gstNumberField;
    private ScrollView scrollView;
    private RoundedImageView profileImage;
    private TextView email;
    private  AuthViewModel authViewModel;
    private Vendor vendor;
    private RelativeLayout layout;
    private boolean permissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Window window = this.getWindow();
        window.setNavigationBarColor(this.getResources().getColor(com.google.zxing.client.android.R.color.zxing_transparent));
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        ScrollView scrollView = findViewById(R.id.scrollView_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        getViewsById();
        Intent intent = getIntent();
        vendor = intent.getParcelableExtra("vendorDetails");
        setData(vendor);
        layout = findViewById(R.id.profile_layout);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        Glide.with(this).load(Constants.URL_API+"vendorProfile/"+vendor.getVendorId()+".jpeg")
                .placeholder(R.drawable.loading).error(R.drawable.no_profile) .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true)).into(profileImage);
        profileImage.setOnClickListener(view -> profileImage.showContextMenu());

        update.setOnClickListener(view -> {
            getStringData();
            if (firstNameString.equals("") || lastNameString.equals("") || phoneString.equals("") ||
                    addressString.equals("") || cityString.equals("") || stateString.equals("") ||
                    businessNameString.equals("") || panNumberString.equals("")) {
                if (firstNameString.equals("")) {
                    firstNameField.setError("First Name is required");
                    scrollView.smoothScrollTo(0, firstNameField.getTop());
                    new Handler().postDelayed(() -> firstNameField.setErrorEnabled(false), 1500);
                } else if (lastNameString.equals("")) {
                    scrollView.smoothScrollTo(0, lastNameField.getTop());
                    lastNameField.setError("Last Name is required");
                    new Handler().postDelayed(() -> lastNameField.setErrorEnabled(false), 1500);
                } else if (phoneString.equals("")) {
                    scrollView.smoothScrollTo(0, phoneField.getTop());
                    phoneField.setError("Phone Number is required");
                    new Handler().postDelayed(() -> phoneField.setErrorEnabled(false), 1500);

                } else if (addressString.equals("")) {
                    scrollView.smoothScrollTo(0, addressField.getTop());
                    addressField.setError("Address is required");
                    new Handler().postDelayed(() -> addressField.setErrorEnabled(false), 1500);
                } else if (cityString.equals("")) {
                    scrollView.smoothScrollTo(0, cityField.getTop());
                    cityField.setError("City is required");
                    new Handler().postDelayed(() -> cityField.setErrorEnabled(false), 1500);
                } else if (stateString.equals("")) {
                    scrollView.smoothScrollTo(0, stateField.getTop());
                    stateField.setError("State is required");
                    new Handler().postDelayed(() -> stateField.setErrorEnabled(false), 1500);
                } else if (businessNameString.equals("")) {
                    scrollView.smoothScrollTo(0, businessNameField.getTop());
                    businessNameField.setError("Business Name is required");
                    new Handler().postDelayed(() -> businessNameField.setErrorEnabled(false), 1500);
                } else if (panNumberString.equals("")) {
                    scrollView.smoothScrollTo(0, panNumberField.getTop());
                    panNumberField.setError("Pan Number is required");
                    new Handler().postDelayed(() -> panNumberField.setErrorEnabled(false), 1500);
                } else if (phoneString.length() != 10) {
                    scrollView.smoothScrollTo(0, phoneField.getTop());
                    phoneField.setError("Phone Number must be 10 digits");
                    new Handler().postDelayed(() -> phoneField.setErrorEnabled(false), 1500);
                }
            } else {
                Vendor vendor1 = new Vendor(
                        firstNameString,
                        lastNameString,
                        vendor.getVendorId(),
                        businessNameString,
                        phoneString,
                        panNumberString,
                        gstNumberString,
                        addressString,
                        cityString,
                        stateString,
                        tollFreeString,
                        websiteString,
                        descriptionString);
                authViewModel.updateProfile(layout, emailString, vendor1).observe(EditProfileActivity.this, vendor -> setData(vendor));
            }
        });

        change_pass.setOnClickListener(view -> {
            Intent intent1 = new Intent(EditProfileActivity.this, OtpActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
            intent1.putExtra("email", sharedPreferences.getString("email", ""));
            startActivity(intent1);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        Add_location.setOnClickListener(view -> {
            AddLocationDialog addLocationDialog = new AddLocationDialog(layout,authViewModel, vendor.getVendorId());
            addLocationDialog.setCancelable(false);
            addLocationDialog.setCancelable(true);
            addLocationDialog.show(getSupportFragmentManager(), "Add Location");
        });

    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();

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

    private void getStringData() {
        emailString = vendor.getVendorId();
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

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(profileImage);
            File file = new File( getRealPathFromURI(uri));
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("vendorProfile", file.getName(), reqFile);
            authViewModel.setNewProfilePic(layout,vendor.getVendorId(), body);
        }


    }
    

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_pic:
                Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(intent, 23);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar snackbar = Snackbar.make(layout, "Please grant permission to access storage", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setAction("Grant", v -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        });
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        Snackbar snackbar = Snackbar.make(layout, "Please grant permission to access storage", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setAction("Grant", v -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        });
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }).check();

                return true;
            case R.id.remove_pic:

                    authViewModel.deleteProfilePic(layout, vendor.getVendorId());
                    profileImage.setImageResource(R.drawable.no_profile);

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