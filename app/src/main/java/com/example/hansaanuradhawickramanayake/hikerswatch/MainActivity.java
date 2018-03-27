package com.example.hansaanuradhawickramanayake.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView latitudeTextView, longitudeTextView, accuracyTextView, altitudeTextView, addressTextView;

    LocationManager locationManager;
    LocationListener locationListener;

    Intent mapsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

                mapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
                mapsIntent.putExtra("latitude", location.getLatitude());
                mapsIntent.putExtra("longitude", location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null){

                updateLocationInfo(lastKnownLocation);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

                startListening();
            }

        }
    }

    public void startListening(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
    }

    public void updateLocationInfo(Location location){

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        addressTextView = findViewById(R.id.addressTextView);



        if (String.valueOf(location.getLatitude()) != null){

            latitudeTextView.setText("Latitude : " + location.getLatitude());
        }
        if (String.valueOf(location.getLongitude()) != null){

            longitudeTextView.setText("Longitude : "+ location.getLongitude());
        }
        if (String.valueOf(location.getAccuracy()) != null){
            accuracyTextView.setText("Accuracy : " + location.getAccuracy());
        }
        if (String.valueOf(location.getAltitude()) != null){

            altitudeTextView.setText("Altitude : " + location.getAltitude());
        }


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String address = "Could not find the address :(";

            if (addressList != null && addressList.size() > 0){

                address = "Address :\n";

                if (addressList.get(0).getThoroughfare() != null){

                    address += addressList.get(0).getThoroughfare() + ", ";
                }

                if (addressList.get(0).getLocality() != null){

                    address += addressList.get(0).getLocality() + ", ";
                }

                if (addressList.get(0).getSubAdminArea() != null){

                    address += addressList.get(0).getSubAdminArea() + ", ";
                }

                if (addressList.get(0).getAdminArea() != null){

                    address += addressList.get(0).getAdminArea() + ", ";
                }

                if (addressList.get(0).getCountryName() != null){

                    address += addressList.get(0).getCountryName();
                }

            }

            addressTextView.setText(address);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void getLocation(View view){


        startActivity(mapsIntent);

    }

}
