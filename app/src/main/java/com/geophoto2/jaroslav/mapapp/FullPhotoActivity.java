package com.geophoto2.jaroslav.mapapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class FullPhotoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);

        Bundle extras = getIntent().getBundleExtra("bundle");

        imageView = (ImageView)findViewById(R.id.full_img);
        imageView.setImageBitmap((Bitmap) extras.getParcelable("image"));

        textView = (TextView)findViewById(R.id.full_title);
        textView.setText(extras.getString("title"));
    }

    public void onClickShareButton(View view) {

        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                bitmap, "Image Description", null);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        intent.putExtra(Intent.EXTRA_TEXT, textView.getText().toString());
        startActivity(intent.createChooser(intent, "Share Image"));
    }
}
