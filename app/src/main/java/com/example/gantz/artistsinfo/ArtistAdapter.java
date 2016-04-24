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
        View itemView = convertView;

        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_item, parent, false);
        }

        Artist artist = values.get(position);

        TextView tvName = (TextView) itemView.findViewById(R.id.name);
        TextView tvGenres = (TextView) itemView.findViewById(R.id.genres);
        TextView tvNumbers = (TextView) itemView.findViewById(R.id.numbers);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.icon);

        tvName.setText(artist.name);
        tvGenres.setText(artist.genresToString());
        tvNumbers.setText(String.format("%s, %s", artist.albumsNumberToString(), artist.tracksNumberToString()));

        // Загружаем аватарку.
        Picasso.with(context).load(artist.cover.small).into(imageView);

        return itemView;
    }
}
