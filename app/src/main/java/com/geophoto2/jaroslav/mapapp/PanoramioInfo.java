package com.geophoto2.jaroslav.mapapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 7 on 24.10.2015.
 */
public class PanoramioInfo implements Parcelable {
    private GeoCoords geoCoords;
    private String photoUrl;
    private String photoTitle;
    private Bitmap bPhoto;
    private String idPhoto;

    public PanoramioInfo() {
        geoCoords = new GeoCoords();
        photoUrl = "";
        photoTitle = "";
        idPhoto = "";
    }

    public PanoramioInfo(GeoCoords coords, String photoUrl, String photoTitle, String idPhoto) {
        geoCoords = new GeoCoords(coords);
        this.photoUrl = photoUrl;
        this.photoTitle = photoTitle;
        this.idPhoto = idPhoto;
    }

    protected PanoramioInfo(Parcel in) {
        photoUrl = in.readString();
        photoTitle = in.readString();
        bPhoto = in.readParcelable(Bitmap.class.getClassLoader());
        idPhoto = in.readString();
    }

    public static final Creator<PanoramioInfo> CREATOR = new Creator<PanoramioInfo>() {
        @Override
        public PanoramioInfo createFromParcel(Parcel in) {
            return new PanoramioInfo(in);
        }

        @Override
        public PanoramioInfo[] newArray(int size) {
            return new PanoramioInfo[size];
        }
    };

    public GeoCoords getGeoCoords() {
        return geoCoords;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public void setbPhoto(Bitmap bitmap) {
        bPhoto = bitmap;
    }

    public Bitmap getbPhoto() {
        return bPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoUrl);
        dest.writeString(photoTitle);
        dest.writeParcelable(bPhoto, flags);
        dest.writeString(idPhoto);
    }
}
