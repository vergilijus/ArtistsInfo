package com.example.gantz.artistsinfo.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ArtistsApi {
    @GET("http://download.cdn.yandex.net/mobilization-2016/artists.json")
    Call<ResponseBody> artistsList();
}
