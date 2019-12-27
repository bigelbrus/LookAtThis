package com.bigelbrus.lookatthis.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigelbrus.lookatthis.ui.details.PhotoDetailsFragment;
import com.bigelbrus.lookatthis.R;
import com.bigelbrus.lookatthis.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Photo> photos;
    private FragmentManager fragmentManager;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;

    public SearchPhotosAdapter(FragmentManager manager) {
        photos = new ArrayList<>();
        this.fragmentManager = manager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false));
        } else {
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM) {
            SearchViewHolder viewHolder = (SearchViewHolder) holder;
            Picasso.get().load(photos.get(position).getUrls().getSmall()).into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photos.size() - 1 && isLoadingAdded ? LOADING : ITEM);
    }

    public void setAdapter(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public void add(Photo photo) {
        photos.add(photo);
        notifyItemInserted(photos.size() - 1);
    }

    public void addAll(List<Photo> p) {
        for (Photo i : p) {
            add(i);
        }

    }

    public void remove(Photo photo) {
        int pos = photos.indexOf(photo);
        if (photos.remove(photo)) {
            notifyItemRemoved(pos);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Photo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int pos = photos.size() - 1;
        Photo item = getItem(pos);
        if (item != null) {
            photos.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public Photo getItem(int pos) {
        return photos.get(pos);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;

        SearchViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.search_item_image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Photo photo = photos.get(getAdapterPosition());
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, PhotoDetailsFragment.getInstance(photo.getWidth(), photo.getHeight(), photo.getDescription(), photo.getUrls().getRegular(), photo.getColor()
                    ))
                    .addToBackStack(null)
                    .commit();
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
