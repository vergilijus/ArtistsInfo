package com.example.gantz.artistsinfo.model;


import android.content.Context;

import com.example.gantz.artistsinfo.R;

import java.io.Serializable;

public class Artist implements Serializable {
    public int id;
    public String name;
    public String link;
    public String[] genres;
    public int albums;
    public int tracks;
    public String description;

    public class Cover implements Serializable {
        public String small;
        public String big;
    }

    public Cover cover;

    public Artist(int id, String name, String link, String[] genres, int albums, int tracks, String description) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.genres = genres;
        this.albums = albums;
        this.tracks = tracks;
        this.description = description;
    }

    public String albumsNumberToString(Context context) {
        return albums + context.getString(R.string.albums_number);
    }

    public String tracksNumberToString(Context context) {
        return tracks + context.getString(R.string.tracks_number);
    }

    public String genresToString(Context context) {
        StringBuilder sb = new StringBuilder();
        int length = genres.length;
        if (length < 1) {
            return context.getString(R.string.not_specified);
        }
        for (int i = 0; i < length - 2; ++i) {
            sb.append(genres[i]).append(", ");
        }
        sb.append(genres[length - 1]);
        return sb.toString();
    }

}
