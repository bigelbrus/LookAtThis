package com.bigelbrus.lookatthis.ui.collections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigelbrus.lookatthis.R;
import com.bigelbrus.lookatthis.models.Collection;
import com.bigelbrus.lookatthis.ui.collections.details.CollectionDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Collection> collections;
    private FragmentManager fragmentManager;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;


    public CollectionsAdapter(FragmentManager fragmentManager) {
        collections = new ArrayList<>();
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
            return new CollectionsViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM) {
            CollectionsViewHolder viewHolder = (CollectionsViewHolder) holder;

            Picasso.get().load(collections.get(position).getCoverPhoto().getUrls().getSmall()).into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return collections != null ? collections.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position == collections.size() - 1 ? LOADING : ITEM;
    }

    public void add (Collection collection) {
        collections.add(collection);
        notifyItemInserted(collections.size() - 1);
    }

    public void addAll(List<Collection> list){
        for (Collection c:list) {
            add(c);
        }
    }

    public void remove(Collection collection) {
        int pos = collections.indexOf(collection);
        if (collections.remove(collection)) {
            notifyItemRemoved(pos);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Collection());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int pos = collections.size() - 1;
        Collection item = getItem(pos);
        if (item != null) {
            collections.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public Collection getItem(int pos) {
        return collections.get(pos);
    }

    class CollectionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        public CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.search_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Collection c = collections.get(getAdapterPosition());
            fragmentManager.beginTransaction().add(R.id.fragment_container,
                    CollectionDetailsFragment.newInstance(String.valueOf(c.getId()),c.getTitle()))
                    .addToBackStack(null)
                    .commit();
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
