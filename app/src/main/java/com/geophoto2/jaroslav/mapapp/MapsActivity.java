package com.geophoto2.jaroslav.mapapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText searchText;
    private Button searchButton;
    private YandexGeocoder yandexGeocoder;
    private GeoCoords geoCoords;
    private Panoramio panoramio;
    private ArrayList<PanoramioInfo> aPanoramioInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchText = (EditText)findViewById(R.id.searchText);
        searchButton = (Button)findViewById(R.id.searchButton);

        geoCoords = new GeoCoords();

        panoramio = new Panoramio();
        panoramio.setPanoramioListener(new Panoramio.PanoramioListener() {
            @Override
            public void onPhotoReceived(ArrayList<PanoramioInfo> aPanoramioInfo) {
                if (aPanoramioInfo.size() > 0)
                    new PlacementMarkers().execute(aPanoramioInfo);
                else {
                    Log.e("PANORAMIO", "Photo not found in this place");
                    Toast.makeText(getApplicationContext(), "Photo not found in this place", Toast.LENGTH_LONG).show();
                    setMarkerOnTheSearchLocation();
                }
            }
        });

        yandexGeocoder = new YandexGeocoder();
        yandexGeocoder.setYandexGeocoderListener(new YandexGeocoder.YandexGeocoderListener() {
            @Override
            public void onCoordReceived(GeoCoords coords) {
                geoCoords = coords;
                panoramio.getPhotosOfTheCoords(coords);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void onClickSearch(View view) {

        if (searchText.getText().length() > 0) {
            yandexGeocoder.getGeoCoord(getResources().getString(R.string.yandex_geocoder_url),
                    searchText.getText().toString());
        } else {
            Log.e("MAIN_ACTIVITY", "You must insert place");
            Toast.makeText(getApplicationContext(), "You must insert place", Toast.LENGTH_LONG).show();
        }
    }

    private void placementMarkers(PanoramioInfo panoramioInfo) {
        LatLng location = new LatLng(panoramioInfo.getGeoCoords().getLat(), panoramioInfo.getGeoCoords().getLan());
        try {
            if (panoramioInfo.getbPhoto() == null) {
                return;
            }
            mMap.addMarker(new MarkerOptions()
                    .snippet(panoramioInfo.getPhotoTitle())
                    .title(panoramioInfo.getPhotoTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(panoramioInfo.getbPhoto()))
                    .position(location)
                    .anchor(0.0f, 1.0f));
        } catch (Exception e) {
            Log.e("MAIN_ACTIVITY_1", e.toString());
        }
    }

    private void setMarkerOnTheSearchLocation() {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(geoCoords.getLan(), geoCoords.getLat()))
                .anchor(0.0f, 1.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geoCoords.getLan(), geoCoords.getLat()), 10f));
    }

    public void onClickShowListPhoto(View view) {
        Intent intent = new Intent(MapsActivity.this, ListPhotoActivity.class);
        intent.putParcelableArrayListExtra("aPanoramioInfo", aPanoramioInfo);
        startActivity(intent);
    }

    private class PlacementMarkers extends AsyncTask<ArrayList<PanoramioInfo>, Void, ArrayList<PanoramioInfo>> {

        @Override
        protected ArrayList<PanoramioInfo> doInBackground(ArrayList<PanoramioInfo>... params) {
            try {

                ArrayList<PanoramioInfo> list = params[0];
                for (int index = 0; index < list.size(); index++) {
                    URL url = new URL(list.get(index).getPhotoUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedInputStream inputStraem = new BufferedInputStream(conn.getInputStream());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStraem);
                    list.get(index).setbPhoto(bitmap);
                    conn.disconnect();
                }
                return list;
            } catch (MalformedURLException e) {
                Log.d("MAIN_ACTIVITY", e.toString());
            } catch (Exception e) {
                Log.d("MAIN_ACTIVITY", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PanoramioInfo> result) {

            for (int index = 0; index < result.size(); index++) {
                try {
                    placementMarkers(result.get(index));
                } catch (Exception e) {
                    Log.e("MAIN_ACTIVITY", e.toString());
                }
            }

            setMarkerOnTheSearchLocation();

            aPanoramioInfo = result;
        }
    }
}
