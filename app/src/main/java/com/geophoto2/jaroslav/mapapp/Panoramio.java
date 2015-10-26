package com.geophoto2.jaroslav.mapapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 7 on 24.10.2015.
 */
public class Panoramio {

    public interface PanoramioListener {
        public void onPhotoReceived(ArrayList<PanoramioInfo> aPanoramioInfo);
    }

    private PanoramioListener listener;

    public Panoramio() {
        this.listener = null;
    }

    public void setPanoramioListener(PanoramioListener listener) {
        this.listener = listener;
    }

    public void getPhotosOfTheCoords(GeoCoords coords) {

        if (coords == null)
            return;

        String urlRequest = "http://www.panoramio.com/map/get_panoramas.php?set=public&from=0&to=20&minx=" +
                String.valueOf(coords.getLat() - 0.5f) + "&miny=" + String.valueOf(coords.getLan() - 0.5f) +
                "&maxx=" + String.valueOf(coords.getLat() + 0.5f) + "&maxy=" + String.valueOf(coords.getLan() + 0.5f) +
                "&size=mini_square&mapfilter=true";

        new HttpRequest().execute(urlRequest);
    }

    public class HttpRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedInputStream inputStraem = null;

            try {

                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                inputStraem = new BufferedInputStream( conn.getInputStream() );
                String contentAsString = ReaderDataFrom.readItFromInputStream(inputStraem);

                return contentAsString;

            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    inputStraem.close();
                } catch (IOException e) {
                    Log.e("PANORAMIO", e.toString());
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("PANORAMIO", "post exectute" + result);

            ArrayList<PanoramioInfo> aPanoramioInfo = new ArrayList<PanoramioInfo>();

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("photos");

                for (int iterator = 0; iterator < jsonArray.length(); iterator++) {
                    JSONObject jo = jsonArray.getJSONObject(iterator);
                    aPanoramioInfo.add(iterator, new PanoramioInfo(new GeoCoords(jo.getDouble("latitude"),
                            jo.getDouble("longitude")),
                            jo.getString("photo_file_url"),
                            jo.getString("photo_title"),
                            jo.getString("photo_id")
                    ));
                }

            } catch (JSONException e) {
                Log.e("PANARAMIO", e.toString());
            }

            listener.onPhotoReceived(aPanoramioInfo);
        }
    }
}
