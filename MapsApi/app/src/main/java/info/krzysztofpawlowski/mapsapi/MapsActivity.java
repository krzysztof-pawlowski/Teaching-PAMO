package info.krzysztofpawlowski.mapsapi;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mFromMarker;
    private Marker mToMarker;
    private Geocoder mGeocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGeocoder = new Geocoder(this, Locale.getDefault());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void onToButtonClicked(View view) {
        EditText toEditText = (EditText) findViewById(R.id.to_edit_text);
        LatLng toLatLng = getLatLngOfAddress(toEditText.getText().toString());
        if (toLatLng == null) {
            return;
        }
        if (mToMarker == null) {
            mToMarker = mMap.addMarker(new MarkerOptions().position(toLatLng));;
        }
        else {
            mToMarker.setPosition(toLatLng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(toLatLng));
        drawLineAndShowDistance();
    }

    public void onFromButtonClicked(View view) {
        EditText fromEditText = (EditText) findViewById(R.id.from_edit_text);
        LatLng fromLatLng = getLatLngOfAddress(fromEditText.getText().toString());
        if (fromLatLng == null) {
            return;
        }
        if (mFromMarker == null) {
            mFromMarker = mMap.addMarker(new MarkerOptions().position(fromLatLng));;
        }
        else {
            mFromMarker.setPosition(fromLatLng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fromLatLng));
        drawLineAndShowDistance();
    }

    private LatLng getLatLngOfAddress(String address) {
        List<Address> addresses = null;
        try {
            addresses = mGeocoder.getFromLocationName(address, 5);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Failed to find the address", Toast.LENGTH_SHORT).show();
            return null;
        }
        return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
    }

    private void drawLineAndShowDistance() {
        if (mToMarker != null && mFromMarker != null) {
            float distance = getDistanceBetweenMarkers(mFromMarker, mToMarker);
            TextView distanceTextView = (TextView) findViewById(R.id.distance_text_view);
            distanceTextView.setText(new Float(distance).toString() + "m");


        }
    }

    private float getDistanceBetweenMarkers(Marker from, Marker to) {
        Location fromLocation = new Location("from");
        fromLocation.setLongitude(from.getPosition().longitude);
        fromLocation.setLatitude(from.getPosition().latitude);

        Location toLocation = new Location("to");
        toLocation.setLongitude(to.getPosition().longitude);
        toLocation.setLatitude(to.getPosition().latitude);

        return fromLocation.distanceTo(toLocation);
    }


}
