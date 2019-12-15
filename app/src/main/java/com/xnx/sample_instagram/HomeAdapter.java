package com.xnx.sample_instagram;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private ArrayList<PostingDTO> mDataset;
    private HomeFragment fragment;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;

        public HomeViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeAdapter(HomeFragment fragment, ArrayList<PostingDTO> myDataset) {
        mDataset = myDataset;
        this.fragment = fragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        HomeViewHolder vh = new HomeViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ImageView imageView = holder.cardView.findViewById(R.id.userImageView);
        ImageView goodImageView = holder.cardView.findViewById(R.id.goodImageView);
        TextView goodCountTextView = holder.cardView.findViewById(R.id.goodCountTextView);
        TextView nameAndDescriptionTextView = holder.cardView.findViewById(R.id.nameAndDescriptionTextView);

        //imageView.setImageURI(Uri.parse(mDataset.get(position).getImageUrl()));
        if(mDataset.get(position) != null)
            Glide.with(fragment).load(mDataset.get(position).getImageUrl()).override(1000).into(imageView);

        goodImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        goodCountTextView.setText("좋아요 " + mDataset.get(position).getGood() + "개");
        nameAndDescriptionTextView.setText(mDataset.get(position).getUserId() +
                " " + mDataset.get(position).getDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}