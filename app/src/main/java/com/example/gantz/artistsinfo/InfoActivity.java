package com.example.gantz.artistsinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gantz.artistsinfo.model.Artist;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    private Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = (ImageView) findViewById(R.id.cover);
        TextView tvGenres = (TextView) findViewById(R.id.genres);
        TextView tvDescription = (TextView) findViewById(R.id.description);
        TextView tvNumbers = (TextView) findViewById(R.id.numbers);
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Получаем исполнителя.
        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("CURRENT_ARTIST");

        // Заполняем поля.
        setTitle(artist.name);
        Picasso.with(this).load(artist.cover.big).into(imageView);
        assert (tvGenres != null) && (tvNumbers != null) && (tvDescription != null);
        tvGenres.setText(artist.genresToString());
        tvNumbers.setText(String.format("%s, %s", artist.albumsNumberToString(), artist.tracksNumberToString()));
        tvDescription.setText(artist.description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
