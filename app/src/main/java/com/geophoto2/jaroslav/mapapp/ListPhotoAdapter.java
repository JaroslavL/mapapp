package com.geophoto2.jaroslav.mapapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 7 on 25.10.2015.
 */
public class ListPhotoAdapter extends ArrayAdapter<PanoramioInfo> {

    private final Context context;
    private final List<PanoramioInfo> values;

    public ListPhotoAdapter(Context context, int resource, int textViewResourceId, List<PanoramioInfo> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.values = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View rowView = inflater.inflate(R.layout.list_single, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.img);

        Bitmap bitmap = values.get(position).getbPhoto();
        txtTitle.setText(values.get(position).getPhotoTitle());
        imageView.setImageBitmap(values.get(position).getbPhoto());

        return rowView;
    }
}
