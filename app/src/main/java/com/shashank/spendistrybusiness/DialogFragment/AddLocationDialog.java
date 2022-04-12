package com.shashank.spendistrybusiness.DialogFragment;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Models.Vendor;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

public class AddLocationDialog extends DialogFragment {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    String lat;
    String lng;
    AuthViewModel authViewModel;
    RelativeLayout relativeLayout;
    String email;

    public AddLocationDialog(RelativeLayout relativeLayout,AuthViewModel authViewModel, String email) {
        this.authViewModel = authViewModel;
        this.relativeLayout = relativeLayout;
        this.email = email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_selection_dialog,container, false);

        requireDialog().getWindow().setLayout(800, 1400);
        requireDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        client = LocationServices.getFusedLocationProviderClient(requireContext());
        getLocationFromMap();
        Button btn_set = rootView.findViewById(R.id.btn_set);
        Button btn_cancel = rootView.findViewById(R.id.btn_cancel);

        btn_set.setOnClickListener(v -> {
            if(lat == null || lng == null){
                Snackbar snackbar = Snackbar.make(relativeLayout, "Please select a location", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.cardBlue));
                snackbar.show();
            }else{
                Vendor vendor = new Vendor(lat,lng);
                authViewModel.updateProfile(relativeLayout, email,vendor).observe(requireActivity(), vendor1 -> {
                    if (!vendor1.getLat().equals("")){
                        dismiss();
                    }
                });
            }
        });

        btn_cancel.setOnClickListener(view -> dismiss());
        return rootView;
    }

    private void getLocationFromMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();

            task.addOnSuccessListener(location -> {

                supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
                if (supportMapFragment != null) {
                    supportMapFragment.getMapAsync(googleMap -> {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        lat = String.valueOf(location.getLatitude());
                        lng = String.valueOf(location.getLongitude());
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.aubergine));
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("current location");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25));
                        googleMap.addMarker(markerOptions);

                        googleMap.setOnMapClickListener(latLng1 -> {
                            MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(latLng1).title("Shop Location");
                            googleMap.clear();
                            googleMap.addMarker(marker);
                            lat = String.valueOf(latLng1.latitude);
                            lng = String.valueOf(latLng1.longitude);
                        });
                    });
                }


            });
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
}
