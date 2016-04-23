package com.example.gantz.artistsinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gantz.artistsinfo.model.Artist;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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


        // Получаем объект артиста.
        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("CURRENT_ARTIST");

        // Заполняем поля.
        Picasso.with(this).load(artist.cover.big).into(imageView);
        try {
            tvGenres.setText(artist.genresToString());
            tvNumbers.setText(artist.albumsNumberToString() + ", " + artist.tracksNumberToString());
            tvDescription.setText(artist.description);
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreate: ", e);
        }

    }
}
