package com.bigelbrus.lookatthis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigelbrus.lookatthis.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchPhotosAdapter extends RecyclerView.Adapter<SearchPhotosAdapter.SearchViewHolder> {

    private List<Photo> photos;
    private FragmentManager manager;

    public SearchPhotosAdapter(FragmentManager manager) {
        photos = new ArrayList<>();
        this.manager = manager;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Picasso.get().load(photos.get(position).getUrls().getSmall()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setAdapter(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        SearchViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.search_item_image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Photo photo = photos.get(getAdapterPosition());
            manager.beginTransaction()
                    .add(R.id.fragment_container,PhotoDetailsFragment.getInstance(
                            photo.getWidth(),photo.getHeight(),photo.getDescription(),photo.getUrls().getRegular(),photo.getColor()
                    ))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
