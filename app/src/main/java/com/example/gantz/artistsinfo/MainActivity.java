package com.example.gantz.artistsinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gantz.artistsinfo.api.ArtistsApi;
import com.example.gantz.artistsinfo.dialogs.ErrorDialog;
import com.example.gantz.artistsinfo.model.Artist;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements Callback<ResponseBody>, ErrorDialog.ErrorDialogListener {

    private static final String TAG = "MainActivity";
    public static final String KEY_CURRENT_ARTIST = "CURRENT_ARTIST";
    private static final String FILE_NAME = "artists.json";

    private ArtistAdapter adapter;
    private List<Artist> artists;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        assert recyclerView != null;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        assert progressBar != null;
        progressBar.setVisibility(View.GONE);

        artists = new ArrayList<>();
        adapter = new ArtistAdapter(this, artists);

        setTitle(getString(R.string.main_title));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildLayoutPosition(v);
                Intent intent = new Intent(v.getContext(), InfoActivity.class);
                intent.putExtra(KEY_CURRENT_ARTIST, artists.get(position));
                startActivity(intent);
            }
        });

        // Еесли нет сохраненных данных запрашиваем список исполнителей.
        File file = new File(this.getFilesDir(), FILE_NAME);
        if (file.exists()) {
            updateList(getSavedArtists());
        } else {
            requestArtistsList();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_update) {
            requestArtistsList();
            progressBar.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void requestArtistsList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ignored_url")
                .build();

        ArtistsApi myApi = retrofit.create(ArtistsApi.class);
        Call<ResponseBody> call = myApi.artistsList();
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        progressBar.setVisibility(View.GONE);
        try {
            saveArtists(response.body().string());
            updateList(getSavedArtists());
        } catch (IOException e) {
            showErrorDialog(getString(R.string.invalid_response));
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        progressBar.setVisibility(View.GONE);
        showErrorDialog(getString(R.string.network_error));
        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
    }

    void showErrorDialog(String message) {
        DialogFragment newFragment = ErrorDialog.newInstance(message);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onNegativeClick() {
        // ...
    }

    @Override
    public void onPositiveClick() {
        requestArtistsList();
    }


    private void saveArtists(String data) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private List<Artist> getSavedArtists() {
        Artist[] artists = null;
        try {
            FileInputStream inputStream = openFileInput(FILE_NAME);
            BufferedInputStream buffInputStream = new BufferedInputStream(inputStream);
            InputStreamReader reader = new InputStreamReader(buffInputStream);
            JsonReader jsonReader = new JsonReader(reader);
            Gson gson = new Gson();
            artists = gson.fromJson(jsonReader, Artist[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Arrays.asList(artists != null ? artists : new Artist[0]);
    }

    private void updateList(List<Artist> newArtistList) {
        artists.clear();
        artists.addAll(newArtistList);
        adapter.notifyDataSetChanged();
    }
}
