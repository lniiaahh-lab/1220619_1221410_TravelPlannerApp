package com.example.travelplannerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.R;
import com.example.travelplannerapp.models.Trip;

import java.util.List;

public class SpecialAdapter
        extends RecyclerView.Adapter<SpecialAdapter.ViewHolder> {

    private List<Trip> trips;
    private OnSpecialActionListener listener;

    public interface OnSpecialActionListener {
        void onDetailsClick(Trip trip);
        void onFavoriteClick(Trip trip);
    }

    public SpecialAdapter(
            List<Trip> trips,
            OnSpecialActionListener listener) {

        this.trips = trips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_special,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Trip trip = trips.get(position);

        holder.tvDestination.setText(
                trip.getDestination());

        holder.tvCountry.setText(
                "📍 " + trip.getCountry());

        holder.tvRating.setText(
                "⭐ Rating: " + trip.getRating());

        holder.btnDetails.setOnClickListener(v ->
                listener.onDetailsClick(trip));

        holder.btnFavorite.setOnClickListener(v ->
                listener.onFavoriteClick(trip));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvDestination;
        TextView tvCountry;
        TextView tvRating;

        Button btnDetails;
        Button btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDestination =
                    itemView.findViewById(R.id.tvDestination);

            tvCountry =
                    itemView.findViewById(R.id.tvCountry);

            tvRating =
                    itemView.findViewById(R.id.tvRating);

            btnDetails =
                    itemView.findViewById(R.id.btnDetails);

            btnFavorite =
                    itemView.findViewById(R.id.btnFavorite);
        }
    }
}