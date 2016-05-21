package com.example.gantz.artistsinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gantz.artistsinfo.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private final Context context;
    private final List<Artist> values;
    private View.OnClickListener clickListener;
//    private ViewHolder.OnItemClickListener onItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener */{
        public TextView tvName;
        public TextView tvGenres;
        public TextView tvNumbers;
        public ImageView imageView;
//        private OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView/*, OnItemClickListener listener*/) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvGenres = (TextView) itemView.findViewById(R.id.genres);
            tvNumbers = (TextView) itemView.findViewById(R.id.numbers);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
//            onItemClickListener = listener;
        }

        /*@Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, );
        }

        public interface OnItemClickListener {
            void onItemClick(View v, int position);
        }*/
    }


    public ArtistAdapter(Context context, List<Artist> values) {
        this.context = context;
        this.values = values;
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        clickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_item, parent, false);
        v.setOnClickListener(clickListener);
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
