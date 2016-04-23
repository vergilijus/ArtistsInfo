package com.example.gantz.artistsinfo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gantz.artistsinfo.api.ArtistsApi;
import com.example.gantz.artistsinfo.model.Artist;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends ListActivity implements Callback<List<Artist>> {

    private static final String TAG = "MainActivity";

    private ArrayAdapter<Artist> adapter;
    private List<Artist> artists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artists = new ArrayList<>();
        adapter = new ArtistAdapter(this, R.layout.list_item, artists);
        setListAdapter(adapter);
        getArtistsList();
    }

    protected void getArtistsList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ignored_url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArtistsApi myApi = retrofit.create(ArtistsApi.class);
        Call<List<Artist>> call = myApi.artistsList();
        call.enqueue(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("CURRENT_ARTIST", artists.get(position));
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
        artists.clear();
        artists.addAll(response.body());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<List<Artist>> call, Throwable t) {
        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
        Toast.makeText(MainActivity.this, "Упс, что то пошло не так.", Toast.LENGTH_SHORT).show();
    }
}
