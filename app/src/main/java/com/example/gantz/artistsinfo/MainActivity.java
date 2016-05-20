package com.example.gantz.artistsinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.gantz.artistsinfo.api.ArtistsApi;
import com.example.gantz.artistsinfo.dialogs.ErrorDialog;
import com.example.gantz.artistsinfo.model.Artist;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements Callback<ResponseBody>, ErrorDialog.ErrorDialogListener {

    private static final String TAG = "MainActivity";
    private static final String PREF_FILE_NAME = "ArtistsPreferences";
    public static final String KEY_CURRENT_ARTIST = "CURRENT_ARTIST";
    public static final String KEY_ARTISTS = "ARTISTS";

    private RecyclerView.Adapter adapter;
    private List<Artist> artists;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        assert recyclerView != null;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        assert progressBar != null;
        progressBar.setVisibility(View.GONE);

        artists = new ArrayList<>();
        adapter = new ArtistAdapter(this, artists);

        setTitle(getString(R.string.main_title));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /*recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), InfoActivity.class);
                intent.putExtra(KEY_CURRENT_ARTIST, artists.get(position));
                startActivity(intent);
            }
        });*/

        // Еесли нет сохраненных данных запрашиваем список исполнителей.
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        if (!preferences.contains(KEY_ARTISTS)) {
            requestArtistsList();
            progressBar.setVisibility(View.VISIBLE);
        } else {
            updateList(getSavedArtists());
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
                .addConverterFactory(GsonConverterFactory.create())
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
        SharedPreferences sharedPref = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_ARTISTS, data);
        editor.apply();
    }

    @NonNull
    private List<Artist> getSavedArtists() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String data = sharedPref.getString(KEY_ARTISTS, "[]");
        Gson gson = new Gson();
        Artist[] artistsList = gson.fromJson(data, Artist[].class);
        return Arrays.asList(artistsList);
    }

    private void updateList(List<Artist> newArtistList) {
        artists.clear();
        artists.addAll(newArtistList);
        adapter.notifyDataSetChanged();
    }
}
