package com.bigelbrus.lookatthis.ui.collections.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigelbrus.lookatthis.R;
import com.bigelbrus.lookatthis.models.Photo;
import com.bigelbrus.lookatthis.ui.details.PhotoDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollectionDetailsAdapter extends RecyclerView.Adapter<CollectionDetailsAdapter.CollectionDetailsViewHolder> {
    private List<Photo> photos;
    private FragmentManager fragmentManager;

    public CollectionDetailsAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        photos = new ArrayList<>();
    }
    @NonNull
    @Override
    public CollectionDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        return new CollectionDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionDetailsViewHolder holder, int position) {
        Picasso.get().load(photos.get(position).getUrls().getSmall()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
    }

    public void setAdapter(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    class CollectionDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        public CollectionDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.search_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Photo photo = photos.get(getAdapterPosition());
            fragmentManager.beginTransaction().add(R.id.fragment_container, PhotoDetailsFragment.getInstance(photo.getWidth(),photo.getHeight(),photo.getDescription(),
                    photo.getUrls().getRegular(),photo.getColor()))
            .addToBackStack(null)
            .commit();
        }
    }
}
