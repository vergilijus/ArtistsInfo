package com.example.gantz.artistsinfo.api;

import com.example.gantz.artistsinfo.model.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArtistsApi {
    @GET("http://download.cdn.yandex.net/mobilization-2016/artists.json")
    Call<List<Artist>> artistsList();
}
