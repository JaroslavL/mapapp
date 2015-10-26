package com.geophoto2.jaroslav.mapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

public class ListPhotoActivity extends AppCompatActivity {

    private ArrayList<PanoramioInfo> aPanoramioInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo);

        aPanoramioInfo = getIntent().getParcelableArrayListExtra("aPanoramioInfo");

        final ListView listView = (ListView)findViewById(R.id.list);
        ListPhotoAdapter listPhotoAdapter = new ListPhotoAdapter(this, R.layout.list_single, R.id.txt, aPanoramioInfo);
        listView.setAdapter(listPhotoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle extras = new Bundle();

                ImageView imageView = (ImageView)view.findViewById(R.id.img);
                TextView textView = (TextView)view.findViewById(R.id.txt);

                imageView.buildDrawingCache();

                extras.putParcelable("image", imageView.getDrawingCache());
                extras.putString("title", textView.getText().toString());

                Intent intent = new Intent(ListPhotoActivity.this, FullPhotoActivity.class);
                intent.putExtra("bundle", extras);
                startActivity(intent);
            }
        });
    }
}
