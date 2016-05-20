package com.example.gantz.artistsinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gantz.artistsinfo.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private final Context context;
    private final List<Artist> values;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvGenres;
        public TextView tvNumbers;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvGenres = (TextView) itemView.findViewById(R.id.genres);
            tvNumbers = (TextView) itemView.findViewById(R.id.numbers);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
        }
    }


    public ArtistAdapter(Context context, List<Artist> values) {
        this.context = context;
        this.values = values;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = values.get(position);
        holder.tvName.setText(artist.name);
        holder.tvGenres.setText(artist.genresToString(context));
        holder.tvNumbers.setText(String.format("%s, %s", artist.albumsNumberToString(context),
                artist.tracksNumberToString(context)));
        // Загружаем аватарку.
        Picasso.with(context).load(artist.cover.small).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
