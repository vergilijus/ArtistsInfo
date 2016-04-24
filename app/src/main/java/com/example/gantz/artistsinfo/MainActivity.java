package com.example.gantz.artistsinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.gantz.artistsinfo.api.ArtistsApi;
import com.example.gantz.artistsinfo.dialogs.ErrorDialog;
import com.example.gantz.artistsinfo.model.Artist;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements Callback<List<Artist>>, ErrorDialog.ErrorDialogListener {

    private static final String TAG = "MainActivity";

    private ArrayAdapter<Artist> adapter;
    private List<Artist> artists;

    private ProgressBar progressBar;

    public static final String KEY_ARTIST = "CURRENT_ARTIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        assert progressBar != null;

        artists = new ArrayList<>();
        adapter = new ArtistAdapter(this, R.layout.list_item, artists);

        setTitle(getString(R.string.main_title));

        assert listView != null;
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), InfoActivity.class);
                intent.putExtra(KEY_ARTIST, artists.get(position));
                startActivity(intent);
            }
        });

        getArtistsList();
    }


    protected void getArtistsList() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ignored_url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArtistsApi myApi = retrofit.create(ArtistsApi.class);
        Call<List<Artist>> call = myApi.artistsList();
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
        artists.clear();
        artists.addAll(response.body());
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Call<List<Artist>> call, Throwable t) {
        progressBar.setVisibility(View.GONE);
        showErrorDialog();
        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
    }

    void showErrorDialog() {
        DialogFragment newFragment = ErrorDialog.newInstance(getString(R.string.unknown_error));
        newFragment.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onNegativeClick() {
        // ...
    }

    @Override
    public void onPositiveClick() {
        getArtistsList();
    }
}
