package com.geophoto2.jaroslav.mapapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 7 on 23.10.2015.
 */
public class YandexGeocoder {

    public interface YandexGeocoderListener {
        public void onCoordReceived(GeoCoords coords);
    }

    private YandexGeocoderListener listener;

    public YandexGeocoder () {
        this.listener = null;
    }

    public void setYandexGeocoderListener(YandexGeocoderListener listener) {
        this.listener = listener;
    }

    public void getGeoCoord(String yandex_geocoder_api, String place) {

        if (place.length() == 0) {
            Log.e("YANDEX_GEOCODER", "You must insert address");
            return;
        }

        String _urlRequest = yandex_geocoder_api + place + "&format=json&result=1";

        new HttpRequest().execute(_urlRequest);
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
                    Log.e("YANDEX_GEOCODER", e.toString());
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                JSONObject jsonObject = new JSONObject(result);
                String sCoord = jsonObject.getJSONObject("response")
                                        .getJSONObject("GeoObjectCollection")
                                        .getJSONArray("featureMember").getJSONObject(0)
                                        .getJSONObject("GeoObject")
                                        .getJSONObject("Point")
                                        .getString("pos");

                String[] aCoords = sCoord.split(" ");

                double lang = Double.parseDouble(aCoords[1]);
                double lat = Double.parseDouble(aCoords[0]);

                GeoCoords geoCoords = new GeoCoords(lat, lang);

                listener.onCoordReceived(geoCoords);

            } catch (JSONException e) {
                Log.e("YANDEX_GEOCODER", e.toString());
            }
        }
    }
}
