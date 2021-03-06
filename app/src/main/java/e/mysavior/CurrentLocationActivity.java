package e.mysavior;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentLocationActivity extends AppCompatActivity implements LocationListener {
    public static String tvLongi;
    public static String tvLati;
    TextView tvLatitude, tvAddress, mtextView;
    TextView tvLongitude;
    LocationManager locationManager;
    TextView t1, t2;
    Location currentLocation;

    double latitude ;//= 13.0864168;
    double longitude ;//= 77.5535868;

    String phoneNo, message, destination;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Lat = "latitude";
    public static final String Longi = "longitude";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        mtextView = (TextView) findViewById(R.id.textView);
        t1 = (TextView) findViewById(R.id.textLat);
        t2 = (TextView) findViewById(R.id.textLong);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        CheckPermission();
        //getLatLang();
        // mySms();


    }


    public void mySms() {

        phoneNo = "8722086222";
        message = "My location is https://maps.google.com/?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        destination = "Nearby hospitals: https://www.google.com/maps/search/hospital/@" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();//+",14z/data=!3m1!4b1";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        smsManager.sendTextMessage(phoneNo, null, destination, null, null);

        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
        try {
            Thread.sleep(500);
            finishAffinity();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        CheckPermission();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            currentLocation = location;
        } else {

            SharedPreferences preferences = getPreferences(MODE_PRIVATE);


            double latitude =  Double.parseDouble(preferences.getString(Lat,"lat"));//13.2032629;
            double longitude =  Double.parseDouble(preferences.getString(Longi,"longi"));
            Toast.makeText(this, latitude+"\n"+longitude, Toast.LENGTH_SHORT).show();
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);
        }

        t1.setText(currentLocation.getLatitude() + "");
        t2.setText(currentLocation.getLongitude() + "");
        if(currentLocation.getLatitude()>0 && currentLocation.getLongitude()>0) {

            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString(Lat, currentLocation.getLatitude()+"" );
            editor.putString(Longi, currentLocation.getLongitude() + "");

            editor.commit();


            t1.setText(currentLocation.getLatitude()+"new" + "");
            t2.setText(currentLocation.getLongitude()+"new" + "");



            mySms();
        }
        // tvLongi = String.valueOf(currentLocation.getLongitude());
        // tvLati = String.valueOf(currentLocation.getLatitude());
        // mtextView.setText(tvLongi);


    }

    private void getLatLang() {

        String result = null;
        if (currentLocation != null) {

            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            result = "Latitude: " + latitude +
                    " Longitude: " + longitude;

        } else {
            result = "Latitude: " + latitude +
                    " Longitude: " + longitude;

        }


        //tvAddress.setText(result);

    }

    public void LatLng(View view) {

        getLatLang();


    }

    public void Address(View view) {

        if (currentLocation != null) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
        }

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(latitude, longitude,
                getApplicationContext(), new GeocoderHandler());


    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            //tvAddress.setText(locationAddress);
        }
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(CurrentLocationActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }
}