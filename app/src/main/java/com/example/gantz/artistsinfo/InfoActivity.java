package com.example.gantz.artistsinfo;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gantz.artistsinfo.model.Artist;
import com.squareup.picasso.Picasso;


public class InfoActivity extends AppCompatActivity {

    private static final String TAG = "InfoActivity";

    private Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageView imageView = (ImageView) findViewById(R.id.cover);
        TextView tvGenres = (TextView) findViewById(R.id.genres);
        TextView tvDescription = (TextView) findViewById(R.id.description);
        TextView tvNumbers = (TextView) findViewById(R.id.numbers);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Получаем исполнителя.
        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("CURRENT_ARTIST");

        // Заполняем поля.
        setTitle(artist.name);
        Picasso.with(this).load(artist.cover.big).into(imageView);
        try {
            tvGenres.setText(artist.genresToString());
            tvNumbers.setText(artist.albumsNumberToString() + ", " + artist.tracksNumberToString());
            tvDescription.setText(artist.description);
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: ", e);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
