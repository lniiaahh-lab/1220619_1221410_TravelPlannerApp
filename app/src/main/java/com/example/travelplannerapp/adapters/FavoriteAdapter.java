package com.example.travelplannerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.R;
import com.example.travelplannerapp.models.Trip;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Trip> favoriteTrips;
    private OnFavoriteActionListener listener;

    public interface OnFavoriteActionListener {
        void onDetailsClick(Trip trip);
        void onReserveClick(Trip trip);
        void onRemoveClick(Trip trip, int position);
    }

    public FavoriteAdapter(List<Trip> favoriteTrips,
                           OnFavoriteActionListener listener) {
        this.favoriteTrips = favoriteTrips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Trip trip = favoriteTrips.get(position);

        holder.tvDestination.setText(trip.getDestination());

        holder.tvCountry.setText("📍 " + trip.getCountry());

        holder.tvRating.setText("⭐ " + trip.getRating());

        holder.tvPrice.setText("$" + trip.getPrice());

        holder.btnDetails.setOnClickListener(v ->
                listener.onDetailsClick(trip));

        holder.btnReserve.setOnClickListener(v ->
                listener.onReserveClick(trip));

        holder.btnRemove.setOnClickListener(v ->
                listener.onRemoveClick(trip,
                        holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return favoriteTrips.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvDestination;
        TextView tvCountry;
        TextView tvRating;
        TextView tvPrice;

        Button btnDetails;
        Button btnReserve;

        ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDestination =
                    itemView.findViewById(R.id.tvDestination);

            tvCountry =
                    itemView.findViewById(R.id.tvCountry);

            tvRating =
                    itemView.findViewById(R.id.tvRating);

            tvPrice =
                    itemView.findViewById(R.id.tvPrice);

            btnDetails =
                    itemView.findViewById(R.id.btnDetails);

            btnReserve =
                    itemView.findViewById(R.id.btnReserve);

            btnRemove =
                    itemView.findViewById(R.id.btnRemove);
        }
    }
}