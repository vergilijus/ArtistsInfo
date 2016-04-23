package com.example.gantz.artistsinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gantz.artistsinfo.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends ArrayAdapter<Artist> {

    private final Context context;
    private final List<Artist> values;

    public ArtistAdapter(Context context, int resource, List<Artist> values) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        Artist artist = values.get(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        TextView tvGenres = (TextView) convertView.findViewById(R.id.genres);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);

        tvName.setText(artist.name);

        // Собираем строку из жанров.
        StringBuilder sb = new StringBuilder();
        for (String genre: artist.genres) {
            sb.append(genre).append(", ");
        }

        tvGenres.setText(sb.toString());

        // Загружаем аватарку.
        Picasso.with(context).setIndicatorsEnabled(true);
        Picasso.with(context).load(artist.cover.small).into(imageView);

        return convertView;
    }
}
