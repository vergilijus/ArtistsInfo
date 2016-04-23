package com.example.gantz.artistsinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.gantz.artistsinfo.model.Artist;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {
    private Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageView imageView = (ImageView) findViewById(R.id.cover);

        // Получаем объект артиста.
        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("CURRENT_ARTIST");

        Picasso.with(this).load(artist.cover.big).into(imageView);

    }
}
