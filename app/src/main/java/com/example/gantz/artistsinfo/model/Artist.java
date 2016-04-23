package com.example.gantz.artistsinfo.model;


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

}
