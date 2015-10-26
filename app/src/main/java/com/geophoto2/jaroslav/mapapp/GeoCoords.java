package com.geophoto2.jaroslav.mapapp;

/**
 * Created by 7 on 24.10.2015.
 */
public class GeoCoords {

    private double lat;
    private double lan;

    public  GeoCoords() {
        lat = 0.0;
        lan = 0.0;
    }

    public GeoCoords(double lat , double lan) {
        this.lat = lat;
        this.lan = lan;
    }

    public GeoCoords(GeoCoords coords) {
        lat = coords.getLat();
        lan = coords.getLan();
    }

    public double getLat() {
        return lat;
    }

    public  double getLan() {
        return lan;
    }
}
